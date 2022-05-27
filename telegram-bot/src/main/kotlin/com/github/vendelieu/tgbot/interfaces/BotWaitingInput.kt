package com.github.vendelieu.tgbot.interfaces

interface BotWaitingInput {
    fun set(telegramId: Long, identifier: String)
    fun get(telegramId: Long): String?
    fun del(telegramId: Long)
}
