package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.BotContext
import eu.vendeli.tgbot.utils.asyncAction
import kotlinx.coroutines.Deferred
import java.util.concurrent.ConcurrentHashMap

abstract class BotContextMapImpl : BotContext {
    protected val storage by lazy { ConcurrentHashMap<String, Any?>() }
    override fun set(telegramId: Long, key: String, value: Any?) {
        storage["$telegramId-$key"] = value
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> = asyncAction {
        storage["$telegramId-$key"] = value
        true
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(telegramId: Long, key: String): T? =
        storage.runCatching { get("$telegramId-$key") as T }.getOrNull()

    override suspend fun <T> getAsync(telegramId: Long, key: String): Deferred<T?> = asyncAction {
        get<T>(telegramId, key)
    }

    override fun del(telegramId: Long, key: String) {
        storage -= "$telegramId-$key"
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = asyncAction {
        storage -= "$telegramId-$key"
        true
    }
}
