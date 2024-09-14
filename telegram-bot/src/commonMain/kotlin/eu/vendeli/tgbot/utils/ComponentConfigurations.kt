package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.LogLvl
import eu.vendeli.tgbot.types.internal.configuration.HttpConfiguration
import eu.vendeli.tgbot.types.internal.configuration.LoggingConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.header
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@InternalApi
fun getConfiguredHttpClient(httpCfg: HttpConfiguration, loggingCfg: LoggingConfiguration) = httpCfg.run cfg@{
    val loggingTag = "eu.vendeli.http"
    HttpClient {
        install("RequestLogging") {
            sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                loggingCfg.logger.log(
                    LogLvl.TRACE,
                    loggingTag,
                    "TgApiRequest: ${context.method} ${context.url.buildString()}",
                    null,
                )
            }
        }

        install(Logging) {
            this.logger = object : Logger {
                override fun log(message: String) {
                    suspend { loggingCfg.logger.log(LogLvl.DEBUG, loggingTag, message, null) }
                }
            }
            level = loggingCfg.httpLogLevel.toKtorLvl()
        }

        install(HttpTimeout) {
            requestTimeoutMillis = this@cfg.requestTimeoutMillis
            connectTimeoutMillis = this@cfg.connectTimeoutMillis
            socketTimeoutMillis = this@cfg.socketTimeoutMillis
        }

        install(HttpRequestRetry) {
            retryStrategy?.let { retryIf(maxRequestRetry, it) } ?: retryOnExceptionOrServerErrors(maxRequestRetry)
            delayMillis { retry ->
                retry * retryDelay
            }
        }

        engine {
            proxy = this@cfg.proxy
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
