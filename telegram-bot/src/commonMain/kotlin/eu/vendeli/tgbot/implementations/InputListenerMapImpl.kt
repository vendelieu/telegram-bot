package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.InputListener
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * [InputListener] implementation based on ConcurrentHashMap<Long, String>
 *
 */
class InputListenerMapImpl : InputListener {
    private val storage by lazy { ConcurrentMap<Long, String>() }

    /**
     * Set new waiting input
     *
     * @param telegramId
     * @param identifier of waiting input
     */
    override fun set(telegramId: Long, identifier: String) {
        storage[telegramId] = identifier
    }

    override suspend fun setAsync(telegramId: Long, identifier: String): Deferred<Boolean> = coroutineScope {
        async {
            set(telegramId, identifier)
            return@async true
        }
    }

    /**
     * Get waiting input of user
     *
     * @param telegramId
     * @return String if there's some or null
     */
    override fun get(telegramId: Long): String? = storage[telegramId]

    override suspend fun getAsync(telegramId: Long): Deferred<String?> = coroutineScope {
        async {
            get(telegramId)
        }
    }

    /**
     * Delete waiting input
     *
     * @param telegramId
     */
    override fun del(telegramId: Long) {
        storage.remove(telegramId)
    }

    override suspend fun delAsync(telegramId: Long): Deferred<Boolean> = coroutineScope {
        async {
            del(telegramId)
            return@async true
        }
    }
}
