package com.github.vendelieu.samples

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.vendelieu.tgbot.interfaces.BotUserData

class UserDataImpl : BotUserData {
    private val storage = Caffeine.newBuilder().weakKeys().build<String, Any?>()

    override fun del(telegramId: Long, key: String): Boolean {
        storage.invalidate("${telegramId}_$key")
        return true
    }

    override fun get(telegramId: Long, key: String): Any? = storage.getIfPresent("${telegramId}_$key")

    override fun set(telegramId: Long, key: String, value: Any?): Boolean {
        storage.put("${telegramId}_$key", value)
        return true
    }
}