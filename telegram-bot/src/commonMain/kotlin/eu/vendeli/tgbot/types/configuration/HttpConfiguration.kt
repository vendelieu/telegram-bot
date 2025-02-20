package eu.vendeli.tgbot.types.configuration

import eu.vendeli.tgbot.annotations.dsl.ConfigurationDSL
import eu.vendeli.tgbot.utils.common.RetryStrategy
import io.ktor.client.engine.ProxyConfig
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
 * i.e., for the base value [maxRequestRetry] the attempts will be in 3, 6, 9 seconds
 * @property proxy Specifies proxy that will be used for http calls.
 * @property additionalHeaders Headers that will be applied to every request.
 */
@Serializable
@ConfigurationDSL
data class HttpConfiguration(
    var requestTimeoutMillis: Long? = null,
    var connectTimeoutMillis: Long? = null,
    var socketTimeoutMillis: Long? = null,
    var maxRequestRetry: Int = 3,
    @Transient
    var retryStrategy: RetryStrategy? = null,
    var retryDelay: Long = 3000L,
    @Transient
    var proxy: ProxyConfig? = null,
    @Transient
    var additionalHeaders: Map<String, Any?>? = null,
) {
    /**
     * Defines a retry strategy that triggers a retry when the server responds with a "Too Many Requests" status.
     *
     */
    fun retryOnTooManyRequests(): RetryStrategy = { _, response ->
        response.status == HttpStatusCode.TooManyRequests
    }

    val HttpResponse.rawStatus: Int get() = status.value
}
