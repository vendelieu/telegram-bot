package eu.vendeli.ktgram.botctx.redis.ctx

import eu.vendeli.tgbot.interfaces.ctx.UserData
import io.lettuce.core.RedisClient
import io.lettuce.core.api.reactive.RedisReactiveCommands
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.future.asDeferred

abstract class UserDataRedisImpl(
    url: String = "redis://localhost",
) : UserData<String> {
    open val redis: RedisReactiveCommands<String, String> = RedisClient.create(url).connect().reactive()

    override fun get(telegramId: Long, key: String): String? = redis.get("userData-$telegramId-$key").block()

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> =
        redis.get("userData-$telegramId-$key").toFuture().asDeferred()

    override fun del(telegramId: Long, key: String): Unit {
        redis.del("userData-$telegramId-$key").block()
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> =
        redis.del("userData-$telegramId-$key").map { it > 0 }.toFuture().asDeferred()

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> =
        redis.set("userData-$telegramId-$key", value ?: "null").map { it == "OK" }.toFuture().asDeferred()

    override fun set(telegramId: Long, key: String, value: String?) {
        redis.set("userData-$telegramId-$key", value ?: "null").block()
    }
}
