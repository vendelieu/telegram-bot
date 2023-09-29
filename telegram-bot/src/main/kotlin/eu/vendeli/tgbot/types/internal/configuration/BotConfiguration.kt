package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.core.ClassManagerImpl
import eu.vendeli.tgbot.core.InputListenerMapImpl
import eu.vendeli.tgbot.core.TokenBucketLimiterImpl
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.InputListener

/**
 * The class containing the bot configuration.
 *
 * @property apiHost Host of telegram api.
 * @property inputListener Input handling class instance.
 * @property classManager The manager that will be used to get classes.
 */
data class BotConfiguration(
    var apiHost: String = "api.telegram.org",
    var inputListener: InputListener = InputListenerMapImpl(),
    var classManager: ClassManager = ClassManagerImpl(),
    internal var rateLimiter: RateLimiterConfiguration = RateLimiterConfiguration(TokenBucketLimiterImpl),
    internal var httpClient: HttpConfiguration = HttpConfiguration(),
    internal var logging: LoggingConfiguration = LoggingConfiguration(),
    internal var updatesListener: UpdatesListenerConfiguration = UpdatesListenerConfiguration(),
    internal var context: ContextConfiguration = ContextConfiguration(),
    internal var commandParsing: CommandParsingConfiguration = CommandParsingConfiguration(),
) {

    /**
     * Function for configuring the http client. See [HttpConfiguration].
     */
    fun httpClient(block: HttpConfiguration.() -> Unit) {
        httpClient.block()
    }

    /**
     * Function for configuring logging. See [LoggingConfiguration].
     */
    fun logging(block: LoggingConfiguration.() -> Unit) {
        logging.block()
    }

    /**
     * Function for configuring requests limiting. See [RateLimiterConfiguration].
     */
    fun rateLimiter(block: RateLimiterConfiguration.() -> Unit) {
        rateLimiter.block()
    }

    /**
     * Function for updates listener configuring. See [UpdatesListenerConfiguration].
     */
    fun updatesListener(block: UpdatesListenerConfiguration.() -> Unit) {
        updatesListener.block()
    }

    /**
     * Function for bot context configuration. See [ContextConfiguration].
     */
    fun context(block: ContextConfiguration.() -> Unit) {
        context.block()
    }

    /**
     * Function for specifying command parsing pattern. See [CommandParsingConfiguration].
     */
    fun commandParsing(block: CommandParsingConfiguration.() -> Unit) {
        commandParsing.block()
    }

    internal fun apply(other: BotConfiguration): BotConfiguration {
        apiHost = other.apiHost
        inputListener = other.inputListener
        classManager = other.classManager
        rateLimiter = other.rateLimiter
        httpClient = other.httpClient
        logging = other.logging
        rateLimiter = other.rateLimiter
        updatesListener = other.updatesListener
        context = other.context
        commandParsing = other.commandParsing

        return this
    }
}
