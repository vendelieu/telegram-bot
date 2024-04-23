package eu.vendeli.spring.starter

import eu.vendeli.tgbot.utils.BotConfigurator

abstract class BotConfiguration {
    open val identifier: String = "KtGram"

    abstract fun applyCfg(): BotConfigurator
}
