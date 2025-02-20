package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.configuration.HttpConfiguration
import eu.vendeli.tgbot.types.configuration.LoggingConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@KtGramInternal
fun getConfiguredHttpClient(httpCfg: HttpConfiguration, loggingCfg: LoggingConfiguration) = httpCfg.run cfg@{
    HttpClient {
        install(Logging) {
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
