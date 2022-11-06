package eu.vendeli.tgbot

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.TelegramBot.Builder
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.core.BotInputListenerMapImpl
import eu.vendeli.tgbot.core.ClassManagerImpl
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.core.TelegramActionsCollector.collect
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.enums.HttpLogLevel
import eu.vendeli.tgbot.interfaces.*
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.TELEGRAM_API_URL_PATTERN
import eu.vendeli.tgbot.utils.TELEGRAM_FILE_URL_PATTERN
import eu.vendeli.tgbot.utils.botHttpRequest
import eu.vendeli.tgbot.utils.convertSuccessResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

/**
 * Telegram bot main instance
 *
 * use [Builder] to create [TelegramBot] instance
 *
 * @property token Token of your bot
 * @property inputListener Input handling instance
 * @property apiHost Host of telegram api
 *
 * @param commandsPackage The place where the search for commands and inputs will be done.
 * @param classManager The manager that will be used to get classes.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class TelegramBot(
    private val token: String,
    commandsPackage: String?,
    val inputListener: BotInputListener,
    classManager: ClassManager,
    private val apiHost: String,
    private val requestTimeoutMillis: Long?,
    private val connectTimeoutMillis: Long?,
    private val socketTimeoutMillis: Long?,
    internal val httpLogLevel: HttpLogLevel,
    internal val maxRequestRetry: Int
) {
    internal val logger = LoggerFactory.getLogger(this::class.java)
    private fun TgMethod.toUrl() = TELEGRAM_API_URL_PATTERN.format(apiHost, token) + name

    internal val magicObjects = mutableMapOf<Class<*>, MagicObject<*>>()

    /**
     * to disable http request logging
     *
     * @see Builder.httpLogLevel
     */
    var disableHttpLogs = false

    /**
     * @property [token] Token of your bot
     */
    class Builder(private val token: String, private val builder: Builder.() -> Unit = {}) {

        /**
         * The manager that will be used to get classes.
         */
        var classManager: ClassManager = ClassManagerImpl()

        /**
         * The place where the search for [CommandHandler], [InputHandler] and [UnprocessedHandler]'s will be done.
         */
        var controllersPackage: String? = null

        /**
         * Host of telegram api
         */
        var apiHost = "api.telegram.org"

        /**
         * Specifies the log the logging level.
         */
        var httpLogLevel = HttpLogLevel.NONE

        /**
         * Input handling instance
         */
        var inputListener: BotInputListener = BotInputListenerMapImpl()

        /**
         * Specifies a request timeout in milliseconds.
         * The request timeout is the time period required to process an HTTP call: from sending a request to receiving a response.
         */
        var requestTimeoutMillis: Long? = null

        /**
         * Specifies a connection timeout in milliseconds.
         * The connection timeout is the time period in which a client should establish a connection with a server.
         */
        var connectTimeoutMillis: Long? = null

        /**
         * Specifies a socket timeout (read and write) in milliseconds. The socket timeout is the maximum time of inactivity between two data packets when exchanging data with a server.
         */
        var socketTimeoutMillis: Long? = null

        /**
         * Specifies a http request maximum retry if had some exceptions
         */
        var maxRequestRetry = 3

        /**
         * Create instance [TelegramBot]
         */
        fun build(): TelegramBot {
            apply(builder)
            if (maxRequestRetry < 0) maxRequestRetry = 0
            return TelegramBot(
                token = token,
                commandsPackage = controllersPackage,
                inputListener = inputListener,
                classManager = classManager,
                apiHost = apiHost,
                httpLogLevel = httpLogLevel,
                requestTimeoutMillis = requestTimeoutMillis,
                connectTimeoutMillis = connectTimeoutMillis,
                socketTimeoutMillis = socketTimeoutMillis,
                maxRequestRetry = maxRequestRetry
            )
        }

    }

    /**
     * Current bot [TelegramUpdateHandler] instance
     */
    val update = TelegramUpdateHandler(commandsPackage?.let { collect(it) }, this, classManager, inputListener)

    /**
     * Parameter to manage UserData, can be set once per instance.
     */
    var userData: BotUserData? = null
        set(value) {
            if (field == null) field = value
        }

    /**
     * Parameter to manage ChatData, can be set once per instance.
     */
    var chatData: BotChatData? = null
        set(value) {
            if (field == null) field = value
        }

    internal var httpClient = HttpClient(CIO) {
        install("RequestLogging") {
            sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                logger.trace("TgApiRequest: {} {}", context.method, context.url.buildString())
            }
        }
        install(Logging) {
            level = LogLevel.valueOf(httpLogLevel.name)
            filter {
                !disableHttpLogs
            }
        }
        install(HttpTimeout) {
            this@TelegramBot.let {
                requestTimeoutMillis =  it.requestTimeoutMillis
                connectTimeoutMillis = it.connectTimeoutMillis
                socketTimeoutMillis = it.socketTimeoutMillis
            }
        }
    }

    /**
     * Gives the ability to expand magical objects
     *
     * @param clazz The class in the final method that will return
     * @param magicObject Implementation of the [MagicObject] interface to be able to generate more contextual object.
     */
    fun <T> addMagicObject(clazz: Class<T>, magicObject: () -> MagicObject<T>) = magicObjects.put(clazz, magicObject())

    /**
     * Get direct url from [File] if [File.filePath] is present
     *
     * @param file
     * @return direct url to file
     */
    fun getFileDirectUrl(file: File): String? =
        if (file.filePath != null) TELEGRAM_FILE_URL_PATTERN.format(apiHost, token, file.filePath)
        else null

    /**
     * Get file from [File] if [File.filePath] is present.
     *
     * @param file
     * @return [ByteArray]
     */
    suspend fun getFileContent(file: File): ByteArray? = if (file.filePath != null) {
        botHttpRequest(TELEGRAM_FILE_URL_PATTERN.format(apiHost, token, file.filePath))?.readBytes()
        //httpClient.get(TELEGRAM_FILE_URL_PATTERN.format(apiHost, token, file.filePath)).readBytes()
    } else null

    /**
     * Function for processing updates by long-pulling using annotation commands.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     */
    suspend fun handleUpdates() {
        update.setListener {
            handle(it)
        }
    }

    /**
     * Function for processing updates by long-pulling using manual handling.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     *
     * @param block [ManualHandlingDsl]
     */
    suspend fun handleUpdates(block: suspend ManualHandlingDsl.() -> Unit) {
        update.setListener {
            handle(it, block)
        }
    }

    private fun multipartBodyBuilder(
        dataField: String,
        filename: String,
        contentType: ContentType,
        data: ByteArray,
        parameters: Map<String, Any?>? = null,
    ) = MultiPartFormDataContent(
        formData {
            appendInput(
                key = dataField,
                headers = Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=$filename")
                    append(HttpHeaders.ContentType, contentType)
                }
            ) { buildPacket { writeFully(data) } }

            parameters?.entries?.forEach { entry ->
                entry.value?.also { append(FormPart(entry.key, mapper.writeValueAsString(it))) }
            }
        }
    )

    private fun <T, I : MultipleResponse> CoroutineScope.handleResponseAsync(
        response: HttpResponse?,
        returnType: Class<T>,
        innerType: Class<I>? = null,
    ): Deferred<Response<out T>> = async {
        val jsonResponse = mapper.readTree(response?.bodyAsText() ?: "")
        logger.debug("Response: ${jsonResponse.toPrettyString()}")

        if (jsonResponse["ok"].asBoolean()) mapper.convertSuccessResponse(jsonResponse, returnType, innerType)
        else mapper.convertValue(jsonResponse, Response.Failure::class.java)
    }

    /**
     * Make a media request with the ability to asynchronously process the response.
     *
     * @param T Generic of response data.
     * @param I Parameter used to identify the type in the data array.
     * @param method The telegram api method to which the request will be made.
     * @param dataField The name of the field that will contain the media data.
     * @param filename The name of the final file.
     * @param data The data itself.
     * @param parameters Additional parameters.
     * @param contentType The type of content that will be passed in the headers.
     * @param returnType Response data type.
     * @param innerType Parameter used to identify the type in the data array.
     * @return [Deferred]<[Response]<[T]>>
     */
    suspend fun <T, I : MultipleResponse> makeRequestAsync(
        method: TgMethod,
        dataField: String,
        filename: String,
        data: ByteArray,
        parameters: Map<String, Any?>? = null,
        contentType: ContentType,
        returnType: Class<T>,
        innerType: Class<I>? = null,
    ): Deferred<Response<out T>> = coroutineScope {
        val response = botHttpRequest(method.toUrl(), HttpMethod.Post) {
            setBody(multipartBodyBuilder(dataField, filename, contentType, data, parameters))
            onUpload { bytesSentTotal, contentLength ->
                logger.trace("Sent $bytesSentTotal bytes from $contentLength, for $method method with $parameters")
            }
        }

        return@coroutineScope handleResponseAsync(response, returnType, innerType)
    }

    /**
     * Make a request with the ability to asynchronously process the response.
     *
     * @param T Generic of response data.
     * @param I Parameter used to identify the type in the data array.
     * @param method The telegram api method to which the request will be made.
     * @param data The data itself.
     * @param returnType Response data type.
     * @param innerType Parameter used to identify the type in the data array.
     * @return [Deferred]<[Response]<[T]>>
     */
    suspend fun <T, I : MultipleResponse> makeRequestAsync(
        method: TgMethod,
        data: Any? = null,
        returnType: Class<T>,
        innerType: Class<I>? = null,
    ): Deferred<Response<out T>> = coroutineScope {
        val response = botHttpRequest(method.toUrl(), HttpMethod.Post) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(data))
        }

        return@coroutineScope handleResponseAsync(response, returnType, innerType)
    }

    /**
     * Make a request without having to return the data.
     *
     * @param method The telegram api method to which the request will be made.
     * @param data The data itself.
     */
    suspend fun makeSilentRequest(method: TgMethod, data: Any? = null) = botHttpRequest(method.toUrl(), HttpMethod.Post) {
        val requestBody = mapper.writeValueAsString(data)
        contentType(ContentType.Application.Json)
        setBody(requestBody)
        logger.debug("RequestBody: $requestBody")
    }

    /**
     * Make a media request without having to return the data.
     *
     * @param method The telegram api method to which the request will be made.
     * @param dataField The name of the field that will contain the media data.
     * @param filename The name of the final file.
     * @param data The data itself.
     * @param parameters Additional parameters.
     * @param contentType The type of content that will be passed in the headers.
     */
    suspend fun makeSilentRequest(
        method: TgMethod,
        dataField: String,
        filename: String,
        data: ByteArray,
        parameters: Map<String, Any?>? = null,
        contentType: ContentType,
    ) = botHttpRequest(method.toUrl(), HttpMethod.Post) {
        setBody(multipartBodyBuilder(dataField, filename, contentType, data, parameters))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace("Sent $bytesSentTotal bytes from $contentLength, for $method method with $parameters")
        }
        logger.debug("RequestBody: $parameters")
    }

    internal suspend fun pullUpdates(offset: Int? = null): List<Update>? {
        logger.trace("Pulling updates.")
        val res = botHttpRequest(TgMethod("getUpdates").toUrl() + (offset?.let { "?offset=$it" } ?: ""), HttpMethod.Post) ?: return null
//        val request = httpClient.post(
//            TgMethod("getUpdates").toUrl() + (offset?.let { "?offset=$it" } ?: "")
//        )
        return mapper.readValue(res.bodyAsText(), jacksonTypeRef<Response<List<Update>>>()).getOrNull()
    }

    companion object {
        internal val mapper = ObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

            registerModules(
                KotlinModule.Builder().apply {
                    withReflectionCacheSize(512)
                    configure(KotlinFeature.NullToEmptyCollection, false)
                    configure(KotlinFeature.NullToEmptyMap, false)
                    configure(KotlinFeature.NullIsSameAsDefault, false)
                    configure(KotlinFeature.SingletonSupport, false)
                    configure(KotlinFeature.StrictNullChecks, false)
                }.build()
            )

            configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
            configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)

            setSerializationInclusion(JsonInclude.Include.NON_NULL)

            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}
