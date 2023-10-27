package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.types.User
import kotlinx.coroutines.Deferred

/**
 * Bot input listener, see
 * [Waiting Input example in article](https://github.com/vendelieu/telegram-bot/wiki/Updates-handling#input-waiting)
 *
 */
interface InputListener {
    /**
     * Set new waiting point
     *
     * @param telegramId
     * @param identifier
     */
    fun set(telegramId: Long, identifier: String)

    /**
     * Shortcut function for [set] method.
     *
     * @param user
     * @param identifier
     */
    fun set(user: User, identifier: () -> String): Unit = set(user.id, identifier())

    /**
     * Set operator shortcut function for setting value.
     *
     * @param user
     * @param identifier
     */
    operator fun set(user: User, identifier: String): Unit = set(user.id, identifier)

    /**
     * Asynchronously set new waiting point
     *
     * @param telegramId
     * @param identifier
     */
    suspend fun setAsync(telegramId: Long, identifier: String): Deferred<Boolean>

    /**
     * Shortcut function for [setAsync] method.
     *
     * @param user
     * @param identifier
     * @return [Deferred]<[Boolean]>
     */
    suspend fun setAsync(user: User, identifier: () -> String): Deferred<Boolean> =
        setAsync(user.id, identifier())

    /**
     * Get current waiting input of user.
     *
     * @param telegramId
     */
    fun get(telegramId: Long): String?

    /**
     * Get operator shortcut function for getting value.
     *
     * @see get
     * @param user
     */
    operator fun get(user: User): String? = get(user.id)

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
