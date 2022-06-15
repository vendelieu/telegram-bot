package com.github.vendelieu.samples

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.api.message

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.github.vendelieu.samples.controller")

    bot.update.setListener {
        handle(it)
        handle(it) {
            onPollAnswer { pollAnswer ->
                println("User#${it.message?.from?.id} chose ${pollAnswer.optionIds}")
            }
        }
    }
}
