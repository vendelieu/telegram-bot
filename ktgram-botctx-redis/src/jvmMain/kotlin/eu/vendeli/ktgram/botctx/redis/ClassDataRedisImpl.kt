package eu.vendeli.ktgram.botctx.redis

import eu.vendeli.tgbot.interfaces.ctx.ClassData
import io.lettuce.core.RedisClient
import io.lettuce.core.api.reactive.RedisReactiveCommands
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.future.asDeferred

abstract class ClassDataRedisImpl(
    url: String = "redis://localhost",
) : ClassData<String> {
    open val redis: RedisReactiveCommands<String, String> = RedisClient.create(url).connect().reactive()

    override fun get(telegramId: Long, key: String): String? = redis.get("classData-$telegramId-$key").block()

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> =
        redis.get("classData-$telegramId-$key").toFuture().asDeferred()

    override fun del(telegramId: Long, key: String) {
        redis.del("classData-$telegramId-$key").block()
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> =
        redis.del("classData-$telegramId-$key").map { it > 0 }.toFuture().asDeferred()

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> =
        redis.set("classData-$telegramId-$key", value ?: "null").map { it == "OK" }.toFuture().asDeferred()

    override suspend fun clearAll(telegramId: Long) {
        redis.keys("classData-$telegramId*").collectList().block()?.let {
            redis.del(*it.toTypedArray())
        }
    }

    override fun set(telegramId: Long, key: String, value: String?) {
        redis.set("classData-$telegramId-$key", value ?: "null").block()
    }
}
