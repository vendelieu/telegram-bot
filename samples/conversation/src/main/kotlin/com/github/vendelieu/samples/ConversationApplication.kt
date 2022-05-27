package com.github.vendelieu.samples

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.api.message

suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", object {}.javaClass.`package`)

    bot.userData = UserDataImpl()

    bot.updateHandler.setListener {
        bot.updateHandler.handleUpdate(it)
    }
}
