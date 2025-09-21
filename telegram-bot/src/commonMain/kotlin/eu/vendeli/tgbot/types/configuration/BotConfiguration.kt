package eu.vendeli.tgbot.types.configuration

import eu.vendeli.tgbot.annotations.dsl.ConfigurationDSL
import eu.vendeli.tgbot.implementations.ClassManagerImpl
import eu.vendeli.tgbot.implementations.InputListenerMapImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.interfaces.ctx.InputListener
import eu.vendeli.tgbot.interfaces.helper.Middleware
import eu.vendeli.tgbot.types.component.ExceptionHandlingStrategy
import eu.vendeli.tgbot.utils.common.ProcessingCtxKey
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
 * @property processingCtxTargets List of targets for which the context will be automatically tracked.
 * @property middlewares List of middlewares that will be applied to all requests.
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
    val processingCtxTargets: MutableSet<ProcessingCtxKey> = mutableSetOf(ProcessingCtxKey.REGEX_MATCH),
    @Transient
    val middlewares: MutableList<Middleware> = mutableListOf(),
    @Transient
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

    internal fun rewriteWith(new: BotConfiguration): BotConfiguration {
        identifier = new.identifier
        apiHost = new.apiHost
        isTestEnv = new.isTestEnv
        inputListener = new.inputListener
        classManager = new.classManager
        inputAutoRemoval = new.inputAutoRemoval
        exceptionHandlingStrategy = new.exceptionHandlingStrategy
        throwExOnActionsFailure = new.throwExOnActionsFailure
        processingCtxTargets.addAll(new.processingCtxTargets)
        middlewares.addAll(new.middlewares)
        rateLimiter = new.rateLimiter
        httpClient = new.httpClient
        logging = new.logging
        rateLimiter = new.rateLimiter
        updatesListener = new.updatesListener
        commandParsing = new.commandParsing

        return this
    }
}
