package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.header
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

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
        retryOnExceptionOrServerErrors(config.httpClient.maxRequestRetry)
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

@OptIn(ExperimentalSerializationApi::class)
val serde = Json {
    namingStrategy = JsonNamingStrategy.SnakeCase
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
}
