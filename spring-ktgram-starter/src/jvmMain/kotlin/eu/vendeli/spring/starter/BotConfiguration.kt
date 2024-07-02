package eu.vendeli.spring.starter

import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.BotConfigurator

abstract class BotConfiguration {
    open val autostartLongPolling = true
    open val identifier: String = "KtGram"
    open val allowedUpdates: List<UpdateType> = emptyList()

    abstract fun applyCfg(): BotConfigurator

    open suspend fun onInit() {}
}
