package eu.vendeli.tgbot.mock

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private val API_PATH = Regex("^/bot([^/]+)/(?:test/)?([^/]+)$")
private val FILE_PATH = Regex("^/file/bot([^/]+)/(.+)$")
private const val CONTROL_PREFIX = "/__mock"

/**
 * A local stand-in for `https://api.telegram.org`. Point a bot at [baseUrl] (`apiHost` in KtGram) and drive it
 * through [bot]. Requests are validated against the scrapped Bot API spec and answered by a stateful core or
 * schema-generated results, see [MockCore].
 */
class MockTelegramServer(
    val config: MockServerConfig = MockServerConfig(),
) : AutoCloseable {
    val core = MockCore(config)
    val spec: ApiSpec get() = core.spec

    private var engine: EmbeddedServer<*, *>? = null

    /** The bound port, valid after [start]. */
    var port: Int = config.port
        private set

    val baseUrl: String get() = "http://${config.host}:$port"

    fun start(): MockTelegramServer {
        val server = embeddedServer(Netty, port = config.port, host = config.host) {
            routing {
                route("{...}") {
                    handle { call.dispatch() }
                }
            }
        }
        server.start(wait = false)
        port = runBlocking {
            server.engine
                .resolvedConnectors()
                .first()
                .port
        }
        engine = server
        return this
    }

    fun bot(token: String = MockServerConfig.DEFAULT_TOKEN): BotHandle = BotHandle(core, core.bot(token))

    /** Runs a call in-process, without HTTP. */
    suspend fun call(token: String, method: String, params: JsonObject = JsonObject(emptyMap())): ApiResult =
        core.execute(token, method, RequestParams(params))

    override fun close() {
        engine?.stop(gracePeriodMillis = 0, timeoutMillis = STOP_TIMEOUT_MS)
        core.close()
    }

    private suspend fun ApplicationCall.dispatch() {
        val path = request.path()
        when {
            path.startsWith(CONTROL_PREFIX) -> {
                ControlApi(this@MockTelegramServer).handle(this, path.removePrefix(CONTROL_PREFIX))
            }

            FILE_PATH.matches(path) -> {
                download(FILE_PATH.matchEntire(path)!!.destructured.let { (t, p) -> t to p })
            }

            else -> {
                val match = API_PATH.matchEntire(path)
                if (match == null) {
                    respondApi(ApiResult.Error(ApiException.notFound()))
                } else {
                    val (token, method) = match.destructured
                    respondApi(callApi(token, method))
                }
            }
        }
    }

    private suspend fun ApplicationCall.callApi(token: String, method: String): ApiResult {
        if (!core.isTokenAccepted(token)) return ApiResult.Error(ApiException.unauthorized())
        val spec = core.spec.method(method) ?: return ApiResult.Error(ApiException.notFound())
        val params = try {
            RequestDecoder.decode(this, spec)
        } catch (e: ApiException) {
            return ApiResult.Error(e)
        }
        return core.execute(token, method, params)
    }

    private suspend fun ApplicationCall.download(target: Pair<String, String>) {
        val (token, filePath) = target
        if (!core.isTokenAccepted(token)) return respondApi(ApiResult.Error(ApiException.unauthorized()))
        val file = core
            .bot(token)
            .files.values
            .firstOrNull { it.path == filePath }
        val bytes = file?.bytes
        if (bytes == null) {
            respondApi(ApiResult.Error(ApiException.notFound()))
        } else {
            respondBytes(bytes, file.mimeType?.let(ContentType::parse) ?: ContentType.Application.OctetStream)
        }
    }

    internal suspend fun ApplicationCall.respondApi(result: ApiResult) = respondJson(result.status, result.toJson())

    internal suspend fun ApplicationCall.respondJson(status: Int, body: JsonElement) =
        respondText(body.toString(), ContentType.Application.Json, HttpStatusCode.fromValue(status))

    internal fun health(): JsonObject = buildJsonObject {
        put("ok", true)
        put("api_version", spec.version)
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 1000L
    }
}
