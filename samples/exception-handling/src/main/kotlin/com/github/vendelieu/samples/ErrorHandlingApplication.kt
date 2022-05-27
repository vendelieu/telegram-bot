package com.github.vendelieu.samples

import com.github.vendelieu.tgbot.TelegramBot

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", object {}.javaClass.`package`)

    bot.update.setListener {
        bot.update.handle(it)?.also { ex -> ExceptionHandler.handleException(it, ex) }
        // Handling method returns an exception if there was one.
    }
}
