package com.github.vendelieu.samples

import com.github.vendelieu.tgbot.TelegramBot

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", object {}.javaClass.`package`)

    bot.updateHandler.setListener {
        bot.updateHandler.handleUpdate(it)?.also { ex -> ExceptionHandler.handleException(it, ex) }
        // Handling method returns an exception if there was one.
    }
}
