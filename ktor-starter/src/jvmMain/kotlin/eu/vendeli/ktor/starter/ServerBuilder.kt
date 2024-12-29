package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.configuration.HttpConfiguration
import eu.vendeli.tgbot.types.internal.configuration.LoggingConfiguration
import eu.vendeli.tgbot.utils.getConfiguredHttpClient
import io.ktor.client.HttpClient
import io.ktor.server.application.Application
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.runBlocking

/**
 * Configuration class for the server.
 *
 * @property WEBHOOK_PREFIX the prefix for webhook routes
 * @property shareHttpClient if the shared http client should be used.
 */
class ServerBuilder internal constructor() {
    /**
     * The declared bot instances.
     */
    internal val botInstances = mutableMapOf<String, TelegramBot>()

    /**
     * The ktor modules to be applied to the server.
     */
    internal val ktorModules = mutableListOf<Application.() -> Unit>()
    internal var server: Configuration? = null
    internal var engineCfg: NettyApplicationEngine.Configuration.() -> Unit = {}
    private var httpClient: HttpClient? = null
    var shareHttpClient: Boolean = false

    @Suppress("ktlint:standard:property-naming")
    var WEBHOOK_PREFIX = "/"

    /**
     * Set the configuration for the server.
     *
     * @param configurator a lambda to configure the server
     */
    fun server(configurator: ManualConfiguration.() -> Unit) {
        server = ManualConfiguration().apply(configurator)
    }

    /**
     * Declare a bot.
     *
     * @param block a lambda to configure the bot
     */
    fun declareBot(block: BotConfiguration.() -> Unit) = BotConfiguration().apply(block).also { cfg ->
        val client = if (shareHttpClient) httpClient ?: getConfiguredHttpClient(
            HttpConfiguration(),
            LoggingConfiguration(),
        ) else null

        botInstances[cfg.token] = TelegramBot(
            token = cfg.token,
            commandsPackage = cfg.pckg,
            httpClient = client?.also {
                httpClient = it
            },
            botConfiguration = cfg.configuration,
        ).also { bot ->
            cfg.handlingBehaviour?.let { bot.update.setBehaviour(it) }
            runBlocking { cfg.onInitHook.invoke(bot) }
        }
    }

    /**
     * Set the configuration for the engine.
     *
     * @param serverConfigurator a lambda to configure the engine
     */
    fun engine(serverConfigurator: NettyApplicationEngine.Configuration.() -> Unit) {
        engineCfg = serverConfigurator
    }

    /**
     * Add a ktor module to the server.
     *
     * @param module a lambda to configure the module
     */
    fun ktorModule(module: Application.() -> Unit) {
        ktorModules.add(module)
    }
}
