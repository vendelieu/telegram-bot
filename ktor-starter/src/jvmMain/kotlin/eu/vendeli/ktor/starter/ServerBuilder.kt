package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import io.ktor.server.netty.NettyApplicationEngine

class ServerBuilder internal constructor() {
    internal var botCfg: BotConfigurator = {}
    internal var serverCfg: NettyApplicationEngine.Configuration.() -> Unit = {}
    internal var handlingBehav: HandlingBehaviourBlock = { handle(it) }

    fun configureBot(botConfigurator: BotConfigurator) {
        botCfg = botConfigurator
    }

    fun configureServer(serverConfigurator: NettyApplicationEngine.Configuration.() -> Unit) {
        serverCfg = serverConfigurator
    }

    fun configureHandlingBehaviour(handlingBehaviour: HandlingBehaviourBlock) {
        handlingBehav = handlingBehaviour
    }
}
