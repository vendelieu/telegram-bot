package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.implementations.ClassManagerImpl
import eu.vendeli.tgbot.implementations.InputListenerMapImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.interfaces.ctx.InputListener

/**
 * The class containing the bot configuration.
 *
 * @property identifier Property to identify different bot instances during multi-bot processing.
 * @property apiHost Host of telegram api.
 * @property isTestEnv Test environment flag.
 * @property inputListener Input handling class instance.
 * @property classManager The manager that will be used to get classes.
 * @property inputAutoRemoval A flag that regulates the auto-deletion of the input point during processing.
 * @property catchExceptions On true > catch raised during processing exceptions and wrap it to caughtExceptions.
 * @property throwExOnActionsFailure Throw exception when the action (any bot request) ends with failure.
 */
data class BotConfiguration(
    var identifier: String = "KtGram",
    var apiHost: String = "https://api.telegram.org",
    var isTestEnv: Boolean = false,
    var inputListener: InputListener = InputListenerMapImpl(),
    var classManager: ClassManager = ClassManagerImpl(),
    var inputAutoRemoval: Boolean = true,
    var catchExceptions: Boolean = true,
    var throwExOnActionsFailure: Boolean = false,
    internal var rateLimiter: RateLimiterConfiguration = RateLimiterConfiguration(),
    internal var httpClient: HttpConfiguration = HttpConfiguration(),
    internal var logging: LoggingConfiguration = LoggingConfiguration(),
    internal var updatesListener: UpdatesListenerConfiguration = UpdatesListenerConfiguration(),
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
     * Function for specifying command parsing pattern. See [CommandParsingConfiguration].
     */
    fun commandParsing(block: CommandParsingConfiguration.() -> Unit) {
        commandParsing.block()
    }

    internal fun apply(new: BotConfiguration): BotConfiguration {
        apiHost = new.apiHost
        inputListener = new.inputListener
        classManager = new.classManager
        rateLimiter = new.rateLimiter
        httpClient = new.httpClient
        logging = new.logging
        rateLimiter = new.rateLimiter
        updatesListener = new.updatesListener
        commandParsing = new.commandParsing

        return this
    }
}
