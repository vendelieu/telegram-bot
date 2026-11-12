package eu.vendeli.tgbot.mock

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.util.concurrent.ConcurrentHashMap

class UploadedFile(
    val fieldName: String,
    val fileName: String?,
    val contentType: String?,
    val bytes: ByteArray,
)

/** Decoded request parameters; uploaded files appear in [values] as `attach://<field>` and in [uploads]. */
class RequestParams(
    val values: JsonObject,
    val uploads: Map<String, UploadedFile> = emptyMap(),
)

/** What a method handler works with. */
class CallContext(
    val core: MockCore,
    val bot: BotState,
    val method: MethodSpec,
    val params: JsonObject,
    val uploads: Map<String, UploadedFile>,
) {
    val clock get() = core.config.clock
    val generator get() = core.generator
    val nowSeconds get() = clock.millis() / MILLIS_IN_SECOND

    fun chat(key: String = "chat_id"): ChatState = bot.resolveChat(params[key])

    private companion object {
        const val MILLIS_IN_SECOND = 1000L
    }
}

internal typealias Handler = suspend CallContext.() -> JsonElement

/** The answer pipeline: faults, stubs, validation, flood control, stateful handlers, generated results. */
class MockCore(
    val config: MockServerConfig,
) {
    val spec: ApiSpec = config.spec ?: ApiSpec.bundled()
    val generator = Generator(spec, config.clock)
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val urlClient: java.net.http.HttpClient by lazy {
        java.net.http.HttpClient
            .newBuilder()
            .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
            .build()
    }
    private val bots = ConcurrentHashMap<String, BotState>()
    private val validator = Validator(spec)
    private val businessRights = BusinessRights(spec)
    private val handlers: Map<String, Handler> = Handlers.all()
    internal val webhooks = WebhookDelivery(this)

    fun bot(token: String): BotState = bots.computeIfAbsent(token) { BotState(it, config) }

    fun isTokenAccepted(token: String): Boolean {
        if (!TOKEN_FORMAT.matches(token)) return false
        return config.allowedTokens?.contains(token) ?: true
    }

    suspend fun execute(token: String, methodName: String, request: RequestParams): ApiResult {
        if (!isTokenAccepted(token)) return ApiResult.Error(ApiException.unauthorized())
        val method = spec.method(methodName) ?: return ApiResult.Error(ApiException.notFound())
        val bot = bot(token)
        val result = try {
            answer(bot, method, request)
        } catch (e: ApiException) {
            ApiResult.Error(e)
        }
        bot.calls += RecordedCall(method.name, request.values, result.status, result.toJson())
        return result
    }

    private suspend fun answer(bot: BotState, method: MethodSpec, request: RequestParams): ApiResult {
        val params = request.values
        bot.faults.firstOrNull { (it.method == null || it.method.equals(method.name, true)) && it.consume() }?.let {
            if (it.delay.isPositive()) delay(it.delay)
            throw it.error
        }
        bot.stubs.firstOrNull { it.matches(method.name, params) && it.consume() }?.let { return it.response }
        // Telegram resolves the business connection before it looks at the other parameters.
        params.str("business_connection_id")?.let { id ->
            val connection = bot.extra.businessConnections[id]
                ?: throw ApiException.badRequest("business connection not found")
            businessRights.needed(method)?.let { businessRights.check(connection, it) }
        }
        if (config.validateRequests) validator.validate(method, params)
        params.str("inline_message_id")?.let {
            if (it !in
                bot.extra.inlineMessages
            ) throw ApiException.badRequest("invalid inline message identifier specified")
        }
        if (config.strictUsers) requireKnownUser(bot, method, params)

        val ctx = CallContext(this, bot, method, params, request.uploads)
        Handlers.resolve(handlers, method)?.let { handler ->
            return ApiResult.Ok(handler(ctx))
        }
        return ApiResult.Ok(generator.forMethod(method))
    }

    private fun requireKnownUser(bot: BotState, method: MethodSpec, params: JsonObject) {
        if (!method.fieldsByName.containsKey("user_id") || method.name.lowercase() in LOOKUP_EXEMPT) return
        val id = params.long("user_id") ?: return
        if (id == bot.id || bot.users.containsKey(id)) return
        val isMemberChange = method.name.lowercase() in PARTICIPANT_METHODS
        throw ApiException.badRequest(if (isMemberChange) "PARTICIPANT_ID_INVALID" else "user not found")
    }

    private fun Stub.matches(name: String, params: JsonObject) =
        method.equals(name, ignoreCase = true) && match.all { (k, v) -> params[k] == v }

    /** Downloads [url] when `fetchUrlFiles` is on, returning bytes and content type; null when fetching is off. */
    fun fetchUrl(url: String): Pair<ByteArray, String?>? {
        if (!config.fetchUrlFiles) return null
        return try {
            val request = java.net.http.HttpRequest
                .newBuilder(java.net.URI.create(url))
                .timeout(java.time.Duration.ofSeconds(FETCH_TIMEOUT_SECONDS))
                .build()
            val response = urlClient.send(
                request,
                java.net.http.HttpResponse.BodyHandlers
                    .ofByteArray(),
            )
            if (response.statusCode() !in HTTP_OK..HTTP_REDIRECT_MIN - 1) {
                throw ApiException.badRequest("wrong file identifier/HTTP URL specified")
            }
            response.body() to response.headers().firstValue("Content-Type").orElse(null)
        } catch (e: java.io.IOException) {
            throw ApiException.badRequest("failed to get HTTP URL content")
        } catch (e: IllegalArgumentException) {
            throw ApiException.badRequest("wrong file identifier/HTTP URL specified")
        }
    }

    fun close() {
        scope.cancel()
    }

    companion object {
        private const val FETCH_TIMEOUT_SECONDS = 10L
        private val PARTICIPANT_METHODS = setOf("banchatmember", "restrictchatmember", "promotechatmember")
        private const val HTTP_REDIRECT_MIN = 300

        /** Telegram checks the charge before the user for these. */
        private val LOOKUP_EXEMPT = setOf("refundstarpayment", "edituserstarsubscription")
        val TOKEN_FORMAT = Regex("^\\d{1,20}:[A-Za-z0-9_-]{30,}$")
    }
}

internal fun JsonElement?.asObject(): JsonObject? = this as? JsonObject
