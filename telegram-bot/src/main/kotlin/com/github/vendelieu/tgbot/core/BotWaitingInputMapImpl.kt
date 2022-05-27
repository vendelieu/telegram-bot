package com.github.vendelieu.tgbot.core

import com.github.vendelieu.tgbot.interfaces.BotWaitingInput
import java.util.*

/**
 * [BotWaitingInput] implementation based on Collections.synchronizedMap(WeakHashMap<>())
 *
 */
class BotWaitingInputMapImpl : BotWaitingInput {
    private val storage = Collections.synchronizedMap(WeakHashMap<Long, String>())

    override fun set(telegramId: Long, identifier: String) {
        storage[telegramId] = identifier
    }

    override fun get(telegramId: Long): String? = storage[telegramId]

    override fun del(telegramId: Long) {
        storage.remove(telegramId)
    }
}
