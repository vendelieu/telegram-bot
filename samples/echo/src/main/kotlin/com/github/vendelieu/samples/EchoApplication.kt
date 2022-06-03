package com.github.vendelieu.samples

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.api.message

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.github.vendelieu.samples.controller")

    bot.update.setListener {
        message(it.message?.text ?: "").send(it.message?.from?.id ?: 0, bot)
    }
}
