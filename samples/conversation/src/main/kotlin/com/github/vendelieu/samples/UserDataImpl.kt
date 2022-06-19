package eu.vendeli.samples

import com.github.benmanes.caffeine.cache.Caffeine
import eu.vendeli.tgbot.interfaces.BotUserData

class UserDataImpl : BotUserData {
    private val storage = Caffeine.newBuilder().weakKeys().build<String, Any?>()

    override fun del(telegramId: Long, key: String) {
        storage.invalidate("${telegramId}_$key")
    }

    override suspend fun delAsync(telegramId: Long, key: String) = coroutineScope {
        async {
            storage.invalidate("${telegramId}_$key")
        }
    }

    override fun get(telegramId: Long, key: String): Any? = storage.getIfPresent("${telegramId}_$key")

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<Any?> = coroutineScope {
        async {
            storage.getIfPresent("${telegramId}_$key")
        }
    }

    override fun set(telegramId: Long, key: String, value: Any?) {
        storage.put("${telegramId}_$key", value)
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> = coroutineScope {
        async {
            storage.put("${telegramId}_$key", value)
        }
    }
}