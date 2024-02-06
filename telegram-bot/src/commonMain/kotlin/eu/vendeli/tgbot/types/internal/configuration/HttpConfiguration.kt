package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.utils.RetryStrategy
import io.ktor.client.engine.ProxyConfig
import io.ktor.client.plugins.HttpTimeout

/**
 * A class containing the configuration for the bot http client.
 *
 * @property requestTimeoutMillis Specifies a request timeout in milliseconds.
 * The request timeout is the time period required to process an HTTP call:
 * from sending a request to receiving a response.
 * @property connectTimeoutMillis Specifies a connection timeout in milliseconds.
 * The connection timeout is the time period in which a client should establish a connection with a server.
 * @property socketTimeoutMillis Specifies a socket timeout (read and write) in milliseconds.
 * The socket timeout is the maximum time of inactivity between two data packets when exchanging data with a server.
 * @property maxRequestRetry Specifies a http request maximum retry if had some exceptions
 * @property retryStrategy By default client uses retryOnExceptionOrServerErrors strategy, but you can define custom.
 * @property retryDelay Multiplier for timeout at each retry, in milliseconds
 * i.e. for the base value [maxRequestRetry] the attempts will be in 3, 6, 9 seconds
 * @property proxy Specifies proxy that will be used for http calls.
 * @property additionalHeaders Headers that will be applied to every request.
 */
data class HttpConfiguration(
    var requestTimeoutMillis: Long = HttpTimeout.INFINITE_TIMEOUT_MS,
    var connectTimeoutMillis: Long = HttpTimeout.INFINITE_TIMEOUT_MS,
    var socketTimeoutMillis: Long = HttpTimeout.INFINITE_TIMEOUT_MS,
    var maxRequestRetry: Int = 3,
    var retryStrategy: RetryStrategy? = null,
    var retryDelay: Long = 3000L,
    var proxy: ProxyConfig? = null,
    var additionalHeaders: Map<String, Any?>? = null,
)
