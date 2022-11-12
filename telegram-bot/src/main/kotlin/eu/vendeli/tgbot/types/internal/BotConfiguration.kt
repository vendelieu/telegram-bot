package eu.vendeli.tgbot.types.internal

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.core.BotInputListenerMapImpl
import eu.vendeli.tgbot.core.ClassManagerImpl
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.types.HttpLogLevel

/**
 * The class containing the bot's configuration.
 *
 * @property apiHost Host of telegram api.
 * @property inputListener Input handling class instance.
 * @property classManager The manager that will be used to get classes.
 */
data class BotConfiguration(
    var apiHost: String = "api.telegram.org",
    var inputListener: BotInputListener = BotInputListenerMapImpl(),
    var classManager: ClassManager = ClassManagerImpl(),
) {
    internal var httpClient = HttpConfiguration()
    internal var logging = LoggingConfiguration()

    /**
     * Function for configuring the http client. See [HttpConfiguration].
     */
    fun httpClient(block: HttpConfiguration.() -> Unit) {
        this.httpClient.block()
    }

    /**
     * Function for configuring logging. See [LoggingConfiguration].
     */
    fun logging(block: LoggingConfiguration.() -> Unit) {
        this.logging.block()
    }
}

/**
 * The class containing the logging configuration.
 *
 * @property botLogLevel The level of logs of the bot's actions.
 * @property httpLogLevel The level of http request logs.
 */
data class LoggingConfiguration(
    var botLogLevel: Level = Level.INFO,
    var httpLogLevel: HttpLogLevel = HttpLogLevel.NONE,
)

/**
 * A class containing the configuration for the bot's http client.
 *
 * @property requestTimeoutMillis Specifies a request timeout in milliseconds.
 * The request timeout is the time period required to process an HTTP call: from sending a request to receiving a response.
 * @property connectTimeoutMillis Specifies a connection timeout in milliseconds.
 * The connection timeout is the time period in which a client should establish a connection with a server.
 * @property socketTimeoutMillis Specifies a socket timeout (read and write) in milliseconds.
 * The socket timeout is the maximum time of inactivity between two data packets when exchanging data with a server.
 * @property maxRequestRetry Specifies a http request maximum retry if had some exceptions
 * @property retryDelay Multiplier for timeout at each retry, in milliseconds
 * i.e. for the base value [maxRequestRetry] the attempts will be in 3, 6, 9 seconds
 */
data class HttpConfiguration(
    var requestTimeoutMillis: Long? = null,
    var connectTimeoutMillis: Long? = null,
    var socketTimeoutMillis: Long? = null,
    var maxRequestRetry: Int = 3,
    var retryDelay: Long = 3000L
)