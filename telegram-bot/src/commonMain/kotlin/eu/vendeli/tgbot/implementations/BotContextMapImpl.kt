package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.BotContext
import eu.vendeli.tgbot.utils.common.asyncAction
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.Deferred

abstract class BotContextMapImpl<T> : BotContext<T> {
    protected val storage by lazy { ConcurrentMap<String, T?>() }
    override fun set(telegramId: Long, key: String, value: T?) {
        storage["$telegramId-$key"] = value
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: T?): Deferred<Boolean> = asyncAction {
        storage["$telegramId-$key"] = value
        true
    }

    override fun get(telegramId: Long, key: String): T? =
        storage["$telegramId-$key"]

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<T?> = asyncAction {
        get(telegramId, key)
    }

    override fun del(telegramId: Long, key: String) {
        storage -= "$telegramId-$key"
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = asyncAction {
        storage -= "$telegramId-$key"
        true
    }
}
