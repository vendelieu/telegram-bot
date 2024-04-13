package eu.vendeli.ktor.starter

import io.ktor.server.netty.NettyApplicationEngine

class ServerBuilder internal constructor() {
    internal val botCfgs: MutableList<BotConfiguration> = mutableListOf()
    internal var server: Configuration? = null
    internal var engineCfg: NettyApplicationEngine.Configuration.() -> Unit = {}

    fun server(configurator: ManualConfiguration.() -> Unit) {
        server = ManualConfiguration().apply(configurator)
    }

    fun declareBot(block: BotConfiguration.() -> Unit) {
        botCfgs += BotConfiguration().apply(block)
    }

    fun engine(serverConfigurator: NettyApplicationEngine.Configuration.() -> Unit) {
        engineCfg = serverConfigurator
    }
}
