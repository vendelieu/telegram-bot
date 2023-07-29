package eu.vendeli.fixtures

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.RegexCommandHandler

class RegexCommands {
    @RegexCommandHandler("test colou?r")
    fun testR(bot: TelegramBot) {
        bot.userData.set(0, "k", "test")
    }
}
