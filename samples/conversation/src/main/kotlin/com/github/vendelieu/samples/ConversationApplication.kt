package eu.vendeli.samples

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "eu.vendeli.samples.controller")
    bot.userData = UserDataImpl()

    bot.handleUpdates()
}
