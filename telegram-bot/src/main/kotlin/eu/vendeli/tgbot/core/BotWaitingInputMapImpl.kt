package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.interfaces.BotWaitingInput
import java.util.*

/**
 * [BotWaitingInput] implementation based on Collections.synchronizedMap(WeakHashMap<>())
 *
 */
class BotWaitingInputMapImpl : BotWaitingInput {
    private val storage = Collections.synchronizedMap(WeakHashMap<Long, String>())

    /**
     * Set new waiting input
     *
     * @param telegramId
     * @param identifier of waiting input
     */
    override fun set(telegramId: Long, identifier: String) {
        storage[telegramId] = identifier
    }

    /**
     * Get waiting input of user
     *
     * @param telegramId
     * @return String if there's some or null
     */
    override fun get(telegramId: Long): String? = storage[telegramId]

    /**
     * Delete waiting input
     *
     * @param telegramId
     */
    override fun del(telegramId: Long) {
        storage.remove(telegramId)
    }
}
