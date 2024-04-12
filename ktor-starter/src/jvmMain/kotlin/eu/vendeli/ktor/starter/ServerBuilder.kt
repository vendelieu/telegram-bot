package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.DEFAULT_HANDLING_BEHAVIOUR
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import io.ktor.server.netty.NettyApplicationEngine

class ServerBuilder internal constructor() {
    internal var server: Configuration? = null
    internal var botCfg: BotConfigurator = {}
    internal var engineCfg: NettyApplicationEngine.Configuration.() -> Unit = {}
    internal var handlingBehav: HandlingBehaviourBlock = DEFAULT_HANDLING_BEHAVIOUR

    fun server(configurator: ManualConfiguration.() -> Unit) {
        server = ManualConfiguration().apply(configurator)
    }

    fun bot(botConfigurator: BotConfigurator) {
        botCfg = botConfigurator
    }

    fun engine(serverConfigurator: NettyApplicationEngine.Configuration.() -> Unit) {
        engineCfg = serverConfigurator
    }

    fun handlingBehaviour(handlingBehaviour: HandlingBehaviourBlock) {
        handlingBehav = handlingBehaviour
    }
}
