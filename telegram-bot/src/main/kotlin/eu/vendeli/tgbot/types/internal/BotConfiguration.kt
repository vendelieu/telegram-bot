package eu.vendeli.tgbot.types.internal

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.core.BotInputListenerMapImpl
import eu.vendeli.tgbot.core.ClassManagerImpl
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.interfaces.ClassManager
import io.ktor.client.plugins.logging.*

data class BotConfiguration(
    var apiHost: String = "api.telegram.org",
    var inputListener: BotInputListener = BotInputListenerMapImpl(),
    var classManager: ClassManager = ClassManagerImpl(),
) {
    internal var httpClient = HttpConfiguration()
    internal var logging = LoggingConfiguration()

    fun httpClient(block: HttpConfiguration.() -> Unit) {
        this.httpClient.block()
    }

    fun logging(block: LoggingConfiguration.() -> Unit) {
        this.logging.block()
    }
}

data class LoggingConfiguration(
    var botLogLevel: Level = Level.INFO,
    var httpLogLevel: LogLevel = LogLevel.NONE,
)

data class HttpConfiguration(
    /**
     * Specifies a request timeout in milliseconds.
     * The request timeout is the time period required to process an HTTP call: from sending a request to receiving a response.
     */
    var requestTimeoutMillis: Long? = null,

    /**
     * Specifies a connection timeout in milliseconds.
     * The connection timeout is the time period in which a client should establish a connection with a server.
     */
    var connectTimeoutMillis: Long? = null,

    /**
     * Specifies a socket timeout (read and write) in milliseconds.
     * The socket timeout is the maximum time of inactivity between two data packets when exchanging data with a server.
     */
    var socketTimeoutMillis: Long? = null,

    /**
     * Specifies a http request maximum retry if had some exceptions
     */
    var maxRequestRetry: Int = 3,

    /**
     * Multiplier for timeout at each retry, in milliseconds.
     *
     * i.e. for the base value [maxRequestRetry] the attempts will be in 3, 6, 9 seconds
     */
    var retryDelay: Long = 3000L
)