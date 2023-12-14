package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ChatData
import eu.vendeli.tgbot.utils.asyncAction
import kotlinx.coroutines.Deferred
import java.util.concurrent.ConcurrentHashMap

object ChatDataMapImpl : ChatData {
    private val storage by lazy { ConcurrentHashMap<String, Any?>() }

    override fun set(telegramId: Long, key: String, value: Any?) {
        storage["$telegramId-$key"] = value
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> = asyncAction {
        storage["$telegramId-$key"] = value
        true
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(telegramId: Long, key: String): T? = storage["$telegramId-$key"] as? T

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> getAsync(telegramId: Long, key: String): Deferred<T?> = asyncAction {
        storage["$telegramId-$key"] as? T
    }

    override fun del(telegramId: Long, key: String) {
        storage -= "$telegramId-$key"
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = asyncAction {
        storage -= "$telegramId-$key"
        true
    }

    override fun clearAll(telegramId: Long) {
        storage.keys.forEach(storage::remove)
    }

    override suspend fun clearAllAsync(telegramId: Long): Deferred<Boolean> = asyncAction {
        storage.keys.forEach(storage::remove)
        true
    }
}
