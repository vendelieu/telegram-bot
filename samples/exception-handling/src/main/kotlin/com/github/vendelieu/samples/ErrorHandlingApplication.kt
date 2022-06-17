package eu.vendeli.samples

import eu.vendeli.tgbot.TelegramBot

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "eu.vendeli.samples.controller")

    bot.update.setListener {
        handle(it)?.also { ex -> ExceptionHandler.handleException(it, ex) }
        // Handling method returns an exception if there was one.
    }
}
