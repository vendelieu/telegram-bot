package eu.vendeli.tgbot.types.configuration

import eu.vendeli.tgbot.annotations.dsl.ConfigurationDSL
import eu.vendeli.tgbot.implementations.ClassManagerImpl
import eu.vendeli.tgbot.implementations.DefaultLoggerFactory
import eu.vendeli.tgbot.implementations.InputListenerMapImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.interfaces.ctx.InputListener
import eu.vendeli.tgbot.interfaces.helper.LoggerFactory
import eu.vendeli.tgbot.types.component.ExceptionHandlingStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The class containing the bot configuration.
 *
 * @property identifier Property to identify different bot instances during multi-bot processing.
 * @property apiHost Host of telegram api.
 * @property isTestEnv Test environment flag.
 * @property inputListener Input handling class instance.
 * @property classManager The manager that will be used to get classes.
 * @property inputAutoRemoval A flag that regulates the auto-deletion of the input point during processing.
 * @property exceptionHandlingStrategy Exception handling strategy. See [ExceptionHandlingStrategy].
 * @property throwExOnActionsFailure Throw exception when the action (any bot request) ends with failure.
 * @property loggerFactory Logger factory. See [LoggerFactory].
 */
@Serializable
@ConfigurationDSL
data class BotConfiguration(
    var identifier: String = "KtGram",
    var apiHost: String = "https://api.telegram.org",
    var isTestEnv: Boolean = false,
    @Transient
    var inputListener: InputListener = InputListenerMapImpl(),
    @Transient
    var classManager: ClassManager = ClassManagerImpl(),
    var inputAutoRemoval: Boolean = true,
    @Transient
    var exceptionHandlingStrategy: ExceptionHandlingStrategy = ExceptionHandlingStrategy.CollectToChannel,
    var throwExOnActionsFailure: Boolean = false,
    @Transient
    internal var rateLimiter: RateLimiterConfiguration = RateLimiterConfiguration(),
    internal var httpClient: HttpConfiguration = HttpConfiguration(),
    internal var updatesListener: UpdatesListenerConfiguration = UpdatesListenerConfiguration(),
    internal var commandParsing: CommandParsingConfiguration = CommandParsingConfiguration(),
    internal var loggerFactory: LoggerFactory = DefaultLoggerFactory,
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
     * Function for specifying command parsing pattern. See [CommandParsingConfiguration].
     */
    fun commandParsing(block: CommandParsingConfiguration.() -> Unit) {
        commandParsing.block()
    }
}

internal fun BotConfiguration.rewriteWith(new: BotConfiguration): BotConfiguration {
    identifier = new.identifier
    apiHost = new.apiHost
    isTestEnv = new.isTestEnv
    inputListener = new.inputListener
    classManager = new.classManager
    inputAutoRemoval = new.inputAutoRemoval
    exceptionHandlingStrategy = new.exceptionHandlingStrategy
    throwExOnActionsFailure = new.throwExOnActionsFailure
    rateLimiter = new.rateLimiter
    httpClient = new.httpClient
    updatesListener = new.updatesListener
    commandParsing = new.commandParsing
    loggerFactory = new.loggerFactory
    return this
}
