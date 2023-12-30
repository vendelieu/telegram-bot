package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.types.MaybeInaccessibleMessage
import eu.vendeli.tgbot.types.MaybeInaccessibleMessage.InaccessibleMessage
import eu.vendeli.tgbot.types.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.header
import io.ktor.http.isSuccess

internal fun TelegramBot.getConfiguredHttpClient() = HttpClient {
    install("RequestLogging") {
        sendPipeline.intercept(HttpSendPipeline.Monitoring) {
            logger.trace { "TgApiRequest: ${context.method} ${context.url.buildString()}" }
        }
    }
    install(Logging) {
        level = config.logging.httpLogLevel.toKtorLvl()
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

    engine {
        proxy = config.httpClient.proxy
    }

    defaultRequest {
        config.httpClient.additionalHeaders?.forEach { (header, value) ->
            header(header, value)
        }
    }
}

internal fun TelegramBot.Companion.getConfiguredMapper() = ObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

    @Suppress("MagicNumber")
    registerModules(
        KotlinModule.Builder().apply {
            withReflectionCacheSize(512)
            configure(KotlinFeature.NullToEmptyCollection, false)
            configure(KotlinFeature.NullToEmptyMap, false)
            configure(KotlinFeature.NullIsSameAsDefault, false)
            configure(KotlinFeature.SingletonSupport, false)
            configure(KotlinFeature.StrictNullChecks, false)
            configure(KotlinFeature.UseJavaDurationConversion, true)
        }.build(),
        JavaTimeModule().enable(JavaTimeFeature.ALWAYS_ALLOW_STRINGIFIED_DATE_TIMESTAMPS),
        SimpleModule().addDeserializer(
            MaybeInaccessibleMessage::class.java,
            MaybeInaccessibleMessageDeser,
        ),
    )

    configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
    configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)

    setSerializationInclusion(JsonInclude.Include.NON_NULL)

    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}

private object MaybeInaccessibleMessageDeser : JsonDeserializer<MaybeInaccessibleMessage>() {
    override fun deserialize(
        jp: JsonParser,
        ctx: DeserializationContext,
    ): MaybeInaccessibleMessage = (jp.codec as ObjectMapper).run {
        val node = readTree<JsonNode>(jp)
        return if (node["date"].asInt() == 0) treeToValue(node, InaccessibleMessage::class.java)
        else treeToValue(node, Message::class.java)
    }
}
