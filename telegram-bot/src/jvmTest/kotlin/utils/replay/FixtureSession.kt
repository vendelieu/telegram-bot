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
import kotlinx.serialization.json.JsonPrimitive
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
 * Exchanges are grouped per test. Inside a test a call is matched by API method **and** request parameters, each
 * recorded exchange is consumed once and identical calls are served in the recorded order, so stateful sequences
 * (create, edit, delete) replay correctly and different inputs get their own responses.
 */
class FixtureSession(
    private val specName: String,
    val mode: TestMode = TestMode.current,
    cassetteProvider: () -> Cassette? = { CassetteStore.load(specName) },
) {
    @Volatile
    var currentTest: String = SPEC_SCOPE

    private val cassette by lazy(cassetteProvider)
    private val replayQueues = ConcurrentHashMap<String, ConcurrentHashMap<String, MutableList<RecordedExchange>>>()
    private val recorded = ConcurrentHashMap<String, MutableList<RecordedExchange>>()
    private val redactor by lazy { Redactor.fromEnv() }

    private fun Redactor.redactParams(params: JsonObject) = redact(params) as JsonObject

    /** A client that answers every request from the recorded fixtures and never touches the network. */
    fun replayClient(): HttpClient = HttpClient(
        MockEngine { request ->
            val key = exchangeKey(request.url)
            val exchange = nextExchange(key, redactor.redactParams(requestParams(request.body)))
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

    private fun nextExchange(key: String, params: JsonObject): RecordedExchange {
        val queue = replayQueues
            .getOrPut(currentTest) {
                ConcurrentHashMap(
                    cassette
                        ?.tests
                        ?.get(currentTest)
                        .orEmpty()
                        .groupBy { it.method }
                        .mapValues { ArrayList(it.value) },
                )
            }.getOrPut(key) { ArrayList() }
        synchronized(queue) {
            val index = queue.indexOfFirst { it.matches(params) }
            if (index >= 0) return queue.removeAt(index)
            throw ReplayMissException(missMessage(key, params, queue.toList()))
        }
    }

    private fun missMessage(key: String, params: JsonObject, candidates: List<RecordedExchange>): String {
        val where = "'$key' in $specName > $currentTest"
        val hint = "Re-record with ./gradlew :telegram-bot:recordFixtures --tests '$specName'"
        if (candidates.isEmpty()) return "No recorded response for $where. $hint"
        val recorded = candidates.joinToString("\n") { "  - ${it.request ?: it.requestKeys}" }
        return "No recorded response for $where matches the sent request $params. " +
            "Still available:\n$recorded\n$hint"
    }

    private fun RecordedExchange.matches(params: JsonObject): Boolean =
        request?.let { it == params } ?: (requestKeys == params.keys.sorted())

    private suspend fun record(request: HttpRequestBuilder, body: ByteArray, status: Int, headers: Headers) {
        val key = exchangeKey(request.url.build())
        val params =
            (request.body as? OutgoingContent)?.let { redactor.redactParams(requestParams(it)) }
                ?: JsonObject(emptyMap())
        val contentType = headers[HttpHeaders.ContentType]
        val exchange = RecordedExchange(
            method = key,
            requestKeys = params.keys.sorted(),
            request = params,
            status = status,
            contentType = contentType,
        ).withBody(body)
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
        const val FILE_PLACEHOLDER = "<file>"
        private const val HEADER_END = "\r\n\r\n"
        private val FILENAME_REGEX = Regex("""filename\*?=""", RegexOption.IGNORE_CASE)
        private val CONTENT_TYPE_REGEX = Regex("""Content-Type:\s*([^\r\n]+)""", RegexOption.IGNORE_CASE)
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

        /**
         * Request parameters by name, whether the body is a JSON object or multipart.
         * Uploaded files are reduced to [FILE_PLACEHOLDER] because their bytes are not stable between runs.
         */
        suspend fun requestParams(body: OutgoingContent): JsonObject {
            if (body is OutgoingContent.NoContent) return JsonObject(emptyMap())
            val raw = body.toByteArray()
            if (raw.isEmpty()) return JsonObject(emptyMap())
            val contentType = body.contentType
            if (contentType?.match(ContentType.Application.Json) == true) {
                val parsed = runCatching { Json.parseToJsonElement(raw.toString(Charsets.UTF_8)) }.getOrNull()
                return parsed as? JsonObject ?: JsonObject(emptyMap())
            }
            val boundary = contentType?.parameter("boundary") ?: return JsonObject(emptyMap())
            return parseMultipart(raw.toString(Charsets.ISO_8859_1), boundary)
        }

        private fun parseMultipart(text: String, boundary: String): JsonObject {
            val fields = text
                .split("--$boundary")
                .mapNotNull { part ->
                    val headEnd = part.indexOf(HEADER_END)
                    if (headEnd < 0) return@mapNotNull null
                    val head = part.substring(0, headEnd)
                    val name = FORM_FIELD_REGEX.find(head)?.groupValues?.get(1) ?: return@mapNotNull null
                    val value = part.substring(headEnd + HEADER_END.length).removeSuffix("\r\n")
                    name to JsonPrimitive(if (head.isFilePart()) FILE_PLACEHOLDER else value.decodeUtf8())
                }.toMap()
            return JsonObject(fields)
        }

        private fun String.isFilePart() = FILENAME_REGEX.containsMatchIn(this) ||
            CONTENT_TYPE_REGEX
                .find(this)
                ?.groupValues
                ?.get(1)
                ?.trim()
                ?.startsWith("text/", ignoreCase = true) == false

        private fun String.decodeUtf8() = toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
    }
}
