package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.implementations.ClassManagerImpl
import eu.vendeli.tgbot.implementations.InputListenerMapImpl
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.internal.LogLvl

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
    var botLogLevel: LogLvl = LogLvl.INFO,
    internal var rateLimiter: RateLimiterConfiguration = RateLimiterConfiguration(),
    internal var httpClient: HttpConfiguration = HttpConfiguration(),
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

    internal fun apply(new: BotConfiguration): BotConfiguration {
        apiHost = new.apiHost
        inputListener = new.inputListener
        classManager = new.classManager
        rateLimiter = new.rateLimiter
        httpClient = new.httpClient
        botLogLevel = new.botLogLevel
        rateLimiter = new.rateLimiter
        updatesListener = new.updatesListener
        context = new.context
        commandParsing = new.commandParsing

        return this
    }
}
