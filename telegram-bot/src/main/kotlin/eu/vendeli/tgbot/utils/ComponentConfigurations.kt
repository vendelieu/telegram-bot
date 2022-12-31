package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

internal fun TelegramBot.getConfiguredHttpClient() = HttpClient(CIO) {
    install("RequestLogging") {
        sendPipeline.intercept(HttpSendPipeline.Monitoring) {
            logger.trace("TgApiRequest: {} {}", context.method, context.url.buildString())
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
}

internal fun TelegramBot.Companion.getConfiguredMapper() = ObjectMapper().apply {
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