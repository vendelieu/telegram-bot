package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.BotContext
import eu.vendeli.tgbot.utils.asyncAction
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.Deferred

abstract class BotContextMapImpl : BotContext<String> {
    protected val storage by lazy { ConcurrentMap<String, String?>() }
    override fun set(telegramId: Long, key: String, value: String?) {
        storage["$telegramId-$key"] = value
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> = asyncAction {
        storage["$telegramId-$key"] = value
        true
    }

    override fun get(telegramId: Long, key: String): String? =
        storage["$telegramId-$key"]

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> = asyncAction {
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
