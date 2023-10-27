package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.types.User
import kotlinx.coroutines.Deferred

interface BotContext {
    /**
     * Set new value.
     *
     * @param telegramId
     * @param key
     * @param value
     */
    fun set(telegramId: Long, key: String, value: Any?)

    /**
     * Shortcut operator function for value setting.
     *
     * @param user
     * @param key
     * @param value
     */
    operator fun set(user: User, key: String, value: Any?): Unit =
        set(user.id, key, value)

    /**
     * Shortcut operator function for setting value.
     * ctx[user] = "value" to "key"
     *
     * @param user
     * @param valToKey
     */
    operator fun set(user: User, valToKey: Pair<Any?, String>): Unit =
        set(user.id, valToKey.second, valToKey.first)

    /**
     * Asynchronously set new value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean>

    /**
     * Shortcut for asynchronously set new value.
     *
     * @param user
     * @param key
     * @param value
     */
    suspend fun setAsync(user: User, key: String, value: () -> Any?): Deferred<Boolean> =
        setAsync(user.id, key, value)

    /**
     * Shortcut for asynchronously set new value.
     * ctx.setAsync(user) { "value" to "key" }
     *
     * @param user
     * @param valToKey
     */
    suspend fun setAsync(
        user: User,
        valToKey: () -> Pair<Any?, String>
    ): Deferred<Boolean> = valToKey().run { setAsync(user.id, second, first) }

    /**
     * Get value
     *
     * @param telegramId
     * @param key
     */
    fun <T> get(telegramId: Long, key: String): T?

    /**
     * Shortcut operator function for getting value.
     *
     * @param T
     * @param user
     * @param key
     */
    operator fun get(user: User, key: String): String? =
        get(user.id, key)

    /**
     * Asynchronously get value
     *
     * @param telegramId
     * @param key
     */
    suspend fun <T> getAsync(telegramId: Long, key: String): Deferred<T?>

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
