package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommonHandler

class RegexCommands {
    @CommonHandler.Regex("test colou?r")
    fun testR(bot: TelegramBot) {
        bot.userData.set(0, "k", "test")
    }
}
