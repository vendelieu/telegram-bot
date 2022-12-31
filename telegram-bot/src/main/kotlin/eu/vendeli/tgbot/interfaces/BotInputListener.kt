package eu.vendeli.tgbot.interfaces

import kotlinx.coroutines.Deferred

/**
 * Bot input listener, see
 * [Waiting Input example in article](https://github.com/vendelieu/telegram-bot/wiki/Updates-handling#input-waiting)
 *
 */
interface BotInputListener {
    /**
     * Set new waiting point
     *
     * @param telegramId
     * @param identifier
     */
    fun set(telegramId: Long, identifier: String)

    /**
     * Asynchronously set new waiting point
     *
     * @param telegramId
     * @param identifier
     */
    suspend fun setAsync(telegramId: Long, identifier: String): Deferred<Boolean>

    /**
     * Get current waiting input of user.
     *
     * @param telegramId
     */
    fun get(telegramId: Long): String?

    /**
     * Asynchronously get current waiting input of user.
     *
     * @param telegramId
     */
    suspend fun getAsync(telegramId: Long): Deferred<String?>

    /**
     * Delete current waiting input of user.
     *
     * @param telegramId
     */
    fun del(telegramId: Long)

    /**
     * Asynchronously delete current waiting input of user.
     *
     * @param telegramId
     */
    suspend fun delAsync(telegramId: Long): Deferred<Boolean>
}
