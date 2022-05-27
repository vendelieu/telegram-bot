package com.github.vendelieu.tgbot

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.github.vendelieu.tgbot.core.BotWaitingInputMapImpl
import com.github.vendelieu.tgbot.core.ClassManagerImpl
import com.github.vendelieu.tgbot.core.TelegramCommandsCollector.collect
import com.github.vendelieu.tgbot.core.TelegramUpdateHandler
import com.github.vendelieu.tgbot.interfaces.*
import com.github.vendelieu.tgbot.types.File
import com.github.vendelieu.tgbot.types.Update
import com.github.vendelieu.tgbot.types.internal.Failure
import com.github.vendelieu.tgbot.types.internal.Response
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.utils.TELEGRAM_API_URL_PATTERN
import com.github.vendelieu.tgbot.utils.TELEGRAM_FILE_URL_PATTERN
import com.github.vendelieu.tgbot.utils.convertSuccessResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
class TelegramBot(
    private val token: String,
    commandsPackage: Package,
    val input: BotWaitingInput = BotWaitingInputMapImpl(),
    classManager: ClassManager = ClassManagerImpl(),
    private val apiHost: String = "api.telegram.org",
) {
    private val logger = LoggerFactory.getLogger("TgRequestLogging")
    private fun TgMethod.toUrl() = TELEGRAM_API_URL_PATTERN.format(apiHost, token) + name

    val update = TelegramUpdateHandler(collect(commandsPackage.name), this, classManager, input)

    var userData: BotUserData? = null
        set(value) {
            if (field == null) field = value
        }
    var chatData: BotChatData? = null
        set(value) {
            if (field == null) field = value
        }

    private val mapper = ObjectMapper().apply {
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

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(mapper))
        }
        install("RequestLogging") {
            sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                logger.trace("TgApiRequest: {} {}", context.method, context.url.buildString())
            }
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
    }

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
        httpClient.get(TELEGRAM_FILE_URL_PATTERN.format(apiHost, token, file.filePath)).readBytes()
    } else null

    /**
     * Make a request with the ability to asynchronously process the response.
     *
     * @param T Generic of response data.
     * @param I Parameter used to identify the type in the data array.
     * @param method
     * @param dataField
     * @param filename
     * @param data
     * @param parameters
     * @param contentType
     * @param returnType Response data type.
     * @param innerType Parameter used to identify the type in the data array.
     * @return [Deferred]<[Response]<[T]>>
     */
    suspend fun <T, I : MultipleResponse> makeRequestAsync(
        method: TgMethod,
        dataField: String,
        filename: String,
        data: ByteArray,
        parameters: Map<String, Any>? = null,
        contentType: ContentType,
        returnType: Class<T>,
        innerType: Class<I>? = null,
    ): Deferred<Response<T>> = coroutineScope {
        val request = httpClient.post(method.toUrl()) {
            contentType(ContentType.Application.Json)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        appendInput(
                            key = dataField,
                            headers = Headers.build {
                                append(HttpHeaders.ContentDisposition, "filename=$filename")
                                append(HttpHeaders.ContentType, contentType)
                            }
                        ) { buildPacket { writeFully(data) } }

                        parameters?.entries?.forEach {
                            append(FormPart(it.key, it.value))
                        }
                    }
                )
            )
        }

        return@coroutineScope async {
            val response = mapper.readTree(request.bodyAsText())
            logger.debug("Response: ${response.toPrettyString()}")

            if (response["ok"].asBoolean()) mapper.convertSuccessResponse(response, returnType, innerType)
            else mapper.convertValue(response, Failure::class.java)
        }
    }

    suspend fun <T, I : MultipleResponse> makeRequestAsync(
        method: TgMethod,
        data: Any? = null,
        returnType: Class<T>,
        innerType: Class<I>? = null,
    ): Deferred<Response<T>> = coroutineScope {
        val request = httpClient.post(method.toUrl()) {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
        return@coroutineScope async {
            val response = mapper.readTree(request.bodyAsText())
            logger.debug("Response: ${response.toPrettyString()}")

            if (response["ok"].asBoolean()) mapper.convertSuccessResponse(response, returnType, innerType)
            else mapper.convertValue(response, Failure::class.java)
        }
    }

    /**
     * Make a query without having to return the data.
     *
     * @param method
     * @param data
     */
    suspend fun makeSilentRequest(method: TgMethod, data: Any? = null) = httpClient.post(method.toUrl()) {
        contentType(ContentType.Application.Json)
        setBody(data)
        logger.debug("RequestBody: ${mapper.writeValueAsString(data)}")
    }

    suspend fun makeSilentRequest(
        method: TgMethod,
        dataField: String,
        filename: String,
        data: ByteArray,
        parameters: Map<String, Any>? = null,
        contentType: ContentType,
    ) = httpClient.post(method.toUrl()) {
        contentType(ContentType.Application.Json)
        setBody(
            MultiPartFormDataContent(
                formData {
                    appendInput(
                        key = dataField,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=$filename")
                            append(HttpHeaders.ContentType, contentType)
                        }
                    ) { buildPacket { writeFully(data) } }

                    parameters?.entries?.forEach {
                        append(FormPart(it.key, it.value))
                    }
                    logger.debug("RequestBody: ${mapper.writeValueAsString(parameters)}")
                }
            )
        )
    }

    internal suspend fun pullUpdates(offset: Int? = null): List<Update>? {
        val request = httpClient.post(TgMethod("getUpdates").toUrl()) {
            contentType(ContentType.Application.Json)
            offset?.also { setBody(mapOf("offset" to it)) }
        }
        val response = mapper.readTree(request.bodyAsText())

        return if (response["ok"].asBoolean()) mapper.convertValue(response["result"], jacksonTypeRef<List<Update>>())
        else null
    }
}
