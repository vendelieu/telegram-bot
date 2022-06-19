package eu.vendeli.tgbot.interfaces

import kotlinx.coroutines.Deferred

/**
 * Bot user data, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
interface BotUserData {
    /**
     * Set new UserData value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    fun set(telegramId: Long, key: String, value: Any?)

    /**
     * Asynchronously set new UserData value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean>

    /**
     * Get UserData value
     *
     * @param telegramId
     * @param key
     */
    fun get(telegramId: Long, key: String): Any?

    /**
     * Asynchronously get UserData value
     *
     * @param telegramId
     * @param key
     */
    suspend fun getAsync(telegramId: Long, key: String): Deferred<Any?>

    /**
     * Del UserData value
     *
     * @param telegramId
     * @param key
     */
    fun del(telegramId: Long, key: String)

    /**
     * Asynchronously delete UserData value
     *
     * @param telegramId
     * @param key
     */
    suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean>
}
