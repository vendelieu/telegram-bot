package utils.replay

import io.ktor.client.HttpClient
import io.ktor.client.call.save
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.util.Base64
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/** Thrown in replay mode when a test performs a request that has no matching recorded exchange. */
class ReplayMissException(
    message: String,
) : AssertionError(message)

/**
 * Serves recorded responses ([TestMode.REPLAY]) or records live ones ([TestMode.RECORD]) for one spec.
 *
 * Exchanges are grouped per test. Inside a test they are matched by API method and consumed in the recorded order
 * per method, so stateful sequences (create, edit, delete) replay correctly even when calls of different methods
 * run concurrently.
 */
class FixtureSession(
    private val specName: String,
    val mode: TestMode = TestMode.current,
    cassetteProvider: () -> Cassette? = { CassetteStore.load(specName) },
) {
    @Volatile
    var currentTest: String = SPEC_SCOPE

    private val cassette by lazy(cassetteProvider)
    private val replayQueues = ConcurrentHashMap<String, ConcurrentHashMap<String, ArrayDeque<RecordedExchange>>>()
    private val recorded = ConcurrentHashMap<String, MutableList<RecordedExchange>>()
    private val redactor by lazy { Redactor.fromEnv() }

    /** A client that answers every request from the recorded fixtures and never touches the network. */
    fun replayClient(): HttpClient = HttpClient(
        MockEngine { request ->
            val key = exchangeKey(request.url)
            val requestKeys = requestKeys(request.body)
            val exchange = nextExchange(key, requestKeys)
            respond(
                content = exchange.bodyBytes(),
                status = HttpStatusCode.fromValue(exchange.status),
                headers = exchange.contentType?.let { headersOf(HttpHeaders.ContentType, it) } ?: headersOf(),
            )
        },
    )

    /** Wraps [live] so that every exchange it performs is recorded. */
    fun recording(live: HttpClient): HttpClient = live.config { }.also { client ->
        client.plugin(HttpSend).intercept { request ->
            val call = execute(request).save()
            record(request, call.response.bodyAsBytes(), call.response.status.value, call.response.headers)
            call
        }
    }

    /** A failed test must not replace a previously recorded good run, so its exchanges are dropped. */
    fun discard(testName: String) {
        recorded.remove(testName)
    }

    /** Writes everything recorded so far, call once the spec is finished. */
    fun flush() {
        if (!mode.isRecord) return
        CassetteStore.merge(specName, recorded.mapValues { it.value.toList() })
    }

    private fun nextExchange(key: String, requestKeys: List<String>): RecordedExchange {
        val queue = replayQueues
            .getOrPut(currentTest) {
                ConcurrentHashMap(
                    cassette
                        ?.tests
                        ?.get(currentTest)
                        .orEmpty()
                        .groupBy { it.method }
                        .mapValues { ArrayDeque(it.value) },
                )
            }.getOrPut(key) { ArrayDeque() }
        val exchange = synchronized(queue) { queue.removeFirstOrNull() }
            ?: throw ReplayMissException(
                "No recorded response for '$key' in $specName > $currentTest. " +
                    "Re-record with ./gradlew :telegram-bot:recordFixtures --tests '$specName'",
            )
        if (exchange.requestKeys != requestKeys) {
            throw ReplayMissException(
                "Request parameters of '$key' changed in $specName > $currentTest: " +
                    "recorded ${exchange.requestKeys}, sent $requestKeys. Re-record the fixtures.",
            )
        }
        return exchange
    }

    private suspend fun record(request: HttpRequestBuilder, body: ByteArray, status: Int, headers: Headers) {
        val key = exchangeKey(request.url.build())
        val keys = (request.body as? OutgoingContent)?.let { requestKeys(it) }.orEmpty()
        val contentType = headers[HttpHeaders.ContentType]
        val exchange = RecordedExchange(key, keys, status, contentType).withBody(body)
        recorded.getOrPut(currentTest) { Collections.synchronizedList(mutableListOf()) }.add(exchange)
    }

    private fun RecordedExchange.withBody(body: ByteArray): RecordedExchange {
        val type = contentType?.let(ContentType::parse)
        return when {
            type?.match(ContentType.Application.Json) == true -> runCatching {
                copy(json = redactor.redact(Json.parseToJsonElement(body.toString(Charsets.UTF_8))))
            }.getOrElse { copy(text = redactor.redact(body.toString(Charsets.UTF_8))) }

            type?.contentType == "text" -> copy(text = redactor.redact(body.toString(Charsets.UTF_8)))

            else -> copy(base64 = Base64.getEncoder().encodeToString(body))
        }
    }

    private fun RecordedExchange.bodyBytes(): ByteArray = when {
        json != null -> json.toString().toByteArray()
        text != null -> text.toByteArray()
        base64 != null -> Base64.getDecoder().decode(base64)
        else -> ByteArray(0)
    }

    companion object {
        const val SPEC_SCOPE = "<spec>"
        private val FORM_FIELD_REGEX =
            Regex("""Content-Disposition:\s*form-data;\s*name="?([^";\r\n]+)"?""", RegexOption.IGNORE_CASE)

        /** `sendMessage` for API calls, `file:<path>` for downloads and `ext:<host/path>` for anything else. */
        fun exchangeKey(url: Url): String {
            val path = url.encodedPath
            return when {
                path.startsWith("/file/bot") -> "file:" + path.removePrefix("/file/bot").substringAfter('/')
                path.startsWith("/bot") -> path.substringAfterLast('/')
                else -> "ext:${url.host}$path"
            }
        }

        /** Sorted names of the request parameters, whether the body is a JSON object or multipart. */
        suspend fun requestKeys(body: OutgoingContent): List<String> {
            if (body is OutgoingContent.NoContent) return emptyList()
            val raw = body.toByteArray()
            if (raw.isEmpty()) return emptyList()
            val asText = raw.toString(Charsets.ISO_8859_1)
            if (body.contentType?.match(ContentType.Application.Json) == true) {
                val parsed = runCatching { Json.parseToJsonElement(raw.toString(Charsets.UTF_8)) }.getOrNull()
                return (parsed as? JsonObject)?.keys?.sorted().orEmpty()
            }
            return FORM_FIELD_REGEX
                .findAll(asText)
                .map { it.groupValues[1] }
                .distinct()
                .sorted()
                .toList()
        }
    }
}
