package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.BotContext
import io.ktor.util.collections.*
import kotlinx.coroutines.*

abstract class BotContextMapImpl<T> : BotContext<T> {
    private val ctxScope = CoroutineScope(Dispatchers.Default + CoroutineName("BotContextMapImpl"))
    protected val storage by lazy { ConcurrentMap<String, T?>() }

    override fun set(telegramId: Long, key: String, value: T?) {
        storage["$telegramId-$key"] = value
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: T?): Deferred<Boolean> = ctxScope.async {
        storage["$telegramId-$key"] = value
        true
    }

    override fun get(telegramId: Long, key: String): T? =
        storage["$telegramId-$key"]

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<T?> = ctxScope.async {
        get(telegramId, key)
    }

    override fun del(telegramId: Long, key: String) {
        storage -= "$telegramId-$key"
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = ctxScope.async {
        storage -= "$telegramId-$key"
        true
    }
}
