package eu.vendeli.tgbot

import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.core.TelegramActionsCollector.collect
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.interfaces.BotChatData
import eu.vendeli.tgbot.interfaces.BotUserData
import eu.vendeli.tgbot.interfaces.MagicObject
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.BotConfiguration
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.TELEGRAM_API_URL_PATTERN
import eu.vendeli.tgbot.utils.TELEGRAM_FILE_URL_PATTERN
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
 * @property token Token of your bot
 *
 * @param commandsPackage The place where the search for commands and inputs will be done.
 * @param botConfiguration Lambda function to customize the bot's instance. Watch [BotConfiguration]
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class TelegramBot(
    private val token: String,
    commandsPackage: String? = null,
    botConfiguration: BotConfiguration.() -> Unit = {}
) {
    private val config = BotConfiguration().apply(botConfiguration)
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        (LoggerFactory.getLogger("eu.vendeli.tgbot") as Logger).level = config.logging.botLogLevel
    }

    private fun TgMethod.toUrl() = TELEGRAM_API_URL_PATTERN.format(config.apiHost, token) + name

    internal val magicObjects = mutableMapOf<Class<*>, MagicObject<*>>()

    /**
     * Current bot [TelegramUpdateHandler] instance
     */
    val update = TelegramUpdateHandler(
        commandsPackage?.let { collect(it) }, this, config.classManager, config.inputListener
    )

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
            level = config.logging.httpLogLevel
        }

        install(HttpTimeout) {
            config.httpClient.also {
                requestTimeoutMillis = it.requestTimeoutMillis
                connectTimeoutMillis = it.connectTimeoutMillis
                socketTimeoutMillis = it.socketTimeoutMillis
            }
        }

        install(HttpRequestRetry) {
            maxRetries = config.httpClient.maxRequestRetry
            retryIf { _, response ->
                !response.status.isSuccess()
            }
            delayMillis { retry ->
                retry * config.httpClient.retryDelay
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
        if (file.filePath != null) TELEGRAM_FILE_URL_PATTERN.format(config.apiHost, token, file.filePath)
        else null

    /**
     * Get file from [File] if [File.filePath] is present.
     *
     * @param file
     * @return [ByteArray]
     */
    suspend fun getFileContent(file: File): ByteArray? = if (file.filePath != null) {
        httpClient.get(TELEGRAM_FILE_URL_PATTERN.format(config.apiHost, token, file.filePath)).readBytes()
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
        response: HttpResponse,
        returnType: Class<T>,
        innerType: Class<I>? = null,
    ): Deferred<Response<out T>> = async {
        val jsonResponse = mapper.readTree(response.bodyAsText())
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
        val response = httpClient.post(method.toUrl()) {
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
        val response = httpClient.post(method.toUrl()) {
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
    suspend fun makeSilentRequest(method: TgMethod, data: Any? = null) = httpClient.post(method.toUrl()) {
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
    ) = httpClient.post(method.toUrl()) {
        setBody(multipartBodyBuilder(dataField, filename, contentType, data, parameters))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace("Sent $bytesSentTotal bytes from $contentLength, for $method method with $parameters")
        }
        logger.debug("RequestBody: $parameters")
    }

    internal suspend fun pullUpdates(offset: Int? = null): List<Update>? {
        logger.trace("Pulling updates.")
        val request = httpClient.post(
            TgMethod("getUpdates").toUrl() + (offset?.let { "?offset=$it" } ?: "")
        )
        return mapper.readValue(request.bodyAsText(), jacksonTypeRef<Response<List<Update>>>()).getOrNull()
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
