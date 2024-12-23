package eu.vendeli.spring.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.BotConfigurator

abstract class BotConfiguration {
    open val autostartLongPolling = true
    open val identifier: String = "KtGram"
    open val allowedUpdates: List<UpdateType>? = null

    abstract fun applyCfg(): BotConfigurator

    open suspend fun onInit(bot: TelegramBot) {}

    open suspend fun onHandlerException(exception: Throwable, bot: TelegramBot) {}
}
