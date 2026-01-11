package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.TelegramBot

interface ContextLoader {
    val pkg: String
        get() = "default"

    fun load(bot: TelegramBot)
}
