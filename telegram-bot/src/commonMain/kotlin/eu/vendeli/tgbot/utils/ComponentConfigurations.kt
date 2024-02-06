package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.header
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

internal fun TelegramBot.getConfiguredHttpClient() = config.httpClient.run {
    HttpClient {
        install("RequestLogging") {
            sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                logger.trace { "TgApiRequest: ${context.method} ${context.url.buildString()}" }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = requestTimeoutMillis
            connectTimeoutMillis = connectTimeoutMillis
            socketTimeoutMillis = socketTimeoutMillis
        }

        install(HttpRequestRetry) {
            retryStrategy?.let { retryIf(maxRequestRetry, it) }
            retryOnExceptionOrServerErrors(maxRequestRetry)
            delayMillis { retry ->
                retry * retryDelay
            }
        }

        engine {
            proxy = this@run.proxy
        }

        defaultRequest {
            additionalHeaders?.forEach { (header, value) ->
                header(header, value)
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal val serde = Json {
    namingStrategy = JsonNamingStrategy.SnakeCase
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
    isLenient = true
}
