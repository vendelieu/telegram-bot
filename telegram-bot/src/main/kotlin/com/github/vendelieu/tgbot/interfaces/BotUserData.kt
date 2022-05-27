package com.github.vendelieu.tgbot.interfaces

interface BotUserData {
    fun set(telegramId: Long, key: String, value: Any?)
    fun get(telegramId: Long, key: String): Any?
    fun del(telegramId: Long, key: String)
}
