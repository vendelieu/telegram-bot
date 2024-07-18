package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
import io.ktor.server.application.Application
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.runBlocking

class ServerBuilder internal constructor() {
    internal val botInstances = mutableMapOf<String, TelegramBot>()
    internal val ktorModules = mutableListOf<Application.() -> Unit>()
    internal var server: Configuration? = null
    internal var engineCfg: NettyApplicationEngine.Configuration.() -> Unit = {}
    var WEBHOOK_PREFIX = "/"

    fun server(configurator: ManualConfiguration.() -> Unit) {
        server = ManualConfiguration().apply(configurator)
    }

    fun declareBot(block: BotConfiguration.() -> Unit) = BotConfiguration().apply(block).also { cfg ->
        botInstances[cfg.token] = TelegramBot(
            cfg.token,
            cfg.pckg,
            cfg.configuration,
        ).also { bot ->
            bot.identifier = cfg.identifier
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
