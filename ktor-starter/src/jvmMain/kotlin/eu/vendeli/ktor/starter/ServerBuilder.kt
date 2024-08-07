package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.utils.getConfiguredHttpClient
import io.ktor.client.HttpClient
import io.ktor.server.application.Application
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.runBlocking

class ServerBuilder internal constructor() {
    internal val botInstances = mutableMapOf<String, TelegramBot>()
    internal val ktorModules = mutableListOf<Application.() -> Unit>()
    internal var server: Configuration? = null
    internal var engineCfg: NettyApplicationEngine.Configuration.() -> Unit = {}
    private var httpClient: HttpClient? = null
    var shareHttpClient: Boolean = false
    var WEBHOOK_PREFIX = "/"

    fun server(configurator: ManualConfiguration.() -> Unit) {
        server = ManualConfiguration().apply(configurator)
    }

    @OptIn(InternalApi::class)
    fun declareBot(block: BotConfiguration.() -> Unit) = BotConfiguration().apply(block).also { cfg ->
        val client = if (shareHttpClient) httpClient ?: getConfiguredHttpClient(
            eu.vendeli.tgbot.types.internal.configuration.BotConfiguration(),
        ) else null

        botInstances[cfg.token] = TelegramBot(
            token = cfg.token,
            commandsPackage = cfg.pckg,
            httpClient = client?.also {
                httpClient = it
            },
            botConfiguration = cfg.configuration,
        ).also { bot ->
            cfg.identifier?.let { bot.identifier = it }
            cfg.handlingBehaviour?.let { bot.update.setBehaviour(it) }
            runBlocking { cfg.onInitHook.invoke(bot) }
        }
    }

    fun engine(serverConfigurator: NettyApplicationEngine.Configuration.() -> Unit) {
        engineCfg = serverConfigurator
    }

    fun ktorModule(module: Application.() -> Unit) {
        ktorModules.add(module)
    }
}
