package eu.vendeli.samples

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "eu.vendeli.samples.controller")

    bot.update.setListener {
        handle(it)
        handle(it) {
            onPollAnswer { pollAnswer ->
                println("User#${it.message?.from?.id} chose ${pollAnswer.optionIds}")
            }
        }
    }
}
