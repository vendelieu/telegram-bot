package eu.vendeli.tgbot.interfaces

import kotlinx.coroutines.Deferred

/**
 * Bot chat data, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
interface ChatData : BotContext {
    /**
     * Clear all chat data entries.
     * Used to remove previous context data.
     *
     * @param telegramId
     */
    fun clearAll(telegramId: Long)

    /**
     * Asynchronously clear all chat data entries.
     * Used to remove previous context data.
     *
     * @param telegramId
     */
    suspend fun clearAllAsync(telegramId: Long): Deferred<Boolean>
}
