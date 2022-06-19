package eu.vendeli.samples

import com.github.benmanes.caffeine.cache.Caffeine
import eu.vendeli.tgbot.interfaces.BotUserData

class BotUserDataImpl(redis: RedissonClient) : BotUserData {
    private val cache = redis.getLocalCachedMap<String, String>(
        "telegramBot-userData", LocalCachedMapOptions.defaults()
    )

    override fun del(telegramId: Long, key: String) {
        cache.fastRemoveAsync("${telegramId}_$key")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = coroutineScope {
        async {
            withContext(Dispatchers.IO) {
                cache.fastRemoveAsync("${telegramId}_$key").get()
            } > 0
        }
    }

    override fun get(telegramId: Long, key: String): Any? = cache["${telegramId}_$key"]

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<Any?> =
        cache.getAsync("${telegramId}_$key").asDeferred()

    override fun set(telegramId: Long, key: String, value: Any?) {
        cache.fastPutAsync("${telegramId}_$key", value.toString())
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> =
        cache.fastPutAsync("${telegramId}_$key", value.toString()).asDeferred()
}