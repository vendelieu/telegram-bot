package eu.vendeli.ktgram.botctx.redis

import com.soywiz.korio.async.async
import com.soywiz.korio.async.runBlockingNoJs
import eu.vendeli.tgbot.interfaces.ctx.UserData
import io.github.jan.rediskm.core.RedisClient
import io.github.jan.rediskm.core.params.get.get
import io.github.jan.rediskm.core.params.misc.delete
import io.github.jan.rediskm.core.params.put.put
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers

abstract class UserDataRedisImpl(
    host: String = "localhost",
    port: Int = 6379,
    user: String? = null,
    password: String? = null,
) : UserData<String> {
    open val redis = RedisClient.create(host, port, password ?: "", user, true)

    override fun get(telegramId: Long, key: String): String? = runBlockingNoJs {
        redis.get<String>("userData-$telegramId-$key")
    }

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> =
        async(Dispatchers.Default) { redis.get<String>("userData-$telegramId-$key") }

    override fun del(telegramId: Long, key: String): Unit = runBlockingNoJs {
        redis.delete("userData-$telegramId-$key")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> =
        async(Dispatchers.Default) { redis.delete("userData-$telegramId-$key") > 0 }

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> =
        async(Dispatchers.Default) {
            redis.put<String>("userData-$telegramId-$key", value ?: "null").value == "OK"
        }

    override fun set(telegramId: Long, key: String, value: String?): Unit = runBlockingNoJs {
        redis.put<String>("userData-$telegramId-$key", value ?: "null")
    }

    suspend fun <T> set(telegramId: Long, key: String, value: T?) {
        redis.put("userData-$telegramId-$key", value ?: "null")
    }

    suspend inline fun <reified T> getValue(telegramId: Long, key: String) =
        redis.get<T>("userData-$telegramId-$key")
}
