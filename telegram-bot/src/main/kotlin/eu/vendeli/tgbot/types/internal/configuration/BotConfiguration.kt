package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.core.BotInputListenerMapImpl
import eu.vendeli.tgbot.core.ClassManagerImpl
import eu.vendeli.tgbot.core.TokenBucketLimiterImpl
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.RateLimitMechanism

/**
 * The class containing the bot configuration.
 *
 * @property apiHost Host of telegram api.
 * @property inputListener Input handling class instance.
 * @property classManager The manager that will be used to get classes.
 */
data class BotConfiguration(
    var apiHost: String = "api.telegram.org",
    var inputListener: BotInputListener = BotInputListenerMapImpl(),
    var classManager: ClassManager = ClassManagerImpl(),
    var rateLimiter: RateLimitMechanism = TokenBucketLimiterImpl(),
) {
    internal val httpClient = HttpConfiguration()
    internal val logging = LoggingConfiguration()
    internal val rateLimits = RateLimits()
    internal val updatesListener = UpdatesListenerConfiguration()
    internal val context = ContextConfiguration()
    internal val commandParsing = CommandParsingConfiguration()

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
     * Function for configuring requests limiting. See [RateLimits].
     */
    fun rateLimits(block: RateLimits.() -> Unit) {
        rateLimits.block()
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
}
