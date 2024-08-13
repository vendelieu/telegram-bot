package eu.vendeli.tgbot.interfaces.ctx

import eu.vendeli.tgbot.types.User
import kotlinx.coroutines.Deferred

/**
 * Bot context parent interface, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
@Suppress("TooManyFunctions")
interface BotContext<T> {
    /**
     * Get value
     *
     * @param telegramId
     * @param key
     */
    operator fun get(telegramId: Long, key: String): T?

    /**
     * Shortcut operator function for getting value.
     *
     * @param T
     * @param user
     * @param key
     */
    operator fun get(user: User, key: String): T? =
        get(user.id, key)

    /**
     * Asynchronously get value
     *
     * @param telegramId
     * @param key
     */
    suspend fun getAsync(telegramId: Long, key: String): Deferred<T?>

    /**
     * Set new value.
     *
     * @param telegramId
     * @param key
     * @param value
     */
    operator fun set(telegramId: Long, key: String, value: T?)

    /**
     * Shortcut operator function for value setting.
     *
     * @param user
     * @param key
     * @param value
     */
    operator fun set(user: User, key: String, value: T?): Unit =
        set(user.id, key, value)

    /**
     * Asynchronously set new value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    suspend fun setAsync(telegramId: Long, key: String, value: T?): Deferred<Boolean>

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
    suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean>
}
