package eu.vendeli.tgbot.interfaces

import kotlinx.coroutines.Deferred

/**
 * Bot chat data, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
interface BotChatData {
    /**
     * Set new ChatData value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    fun set(telegramId: Long, key: String, value: Any?)

    /**
     * Asynchronously set new ChatData value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean>

    /**
     * Get ChatData value
     *
     * @param telegramId
     * @param key
     */
    fun get(telegramId: Long, key: String): Any?

    /**
     * Asynchronously get ChatData value
     *
     * @param telegramId
     * @param key
     */
    fun getAsync(telegramId: Long, key: String): Deferred<Any?>

    /**
     * Delete value
     *
     * @param telegramId
     * @param key
     */
    fun del(telegramId: Long, key: String)


    /**
     * Asynchronously delete value
     *
     * @param telegramId
     * @param key
     */
    fun delAsync(telegramId: Long, key: String): Deferred<Boolean>

    /**
     * Delete previous chat section data, method used to bind data to a class.
     *
     * @param telegramId
     */
    fun delPrevChatSection(telegramId: Long)
}
