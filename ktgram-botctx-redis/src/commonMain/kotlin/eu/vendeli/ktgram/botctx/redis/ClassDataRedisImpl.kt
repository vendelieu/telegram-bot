package eu.vendeli.ktgram.botctx.redis

import com.soywiz.kds.fastCastTo
import com.soywiz.korio.async.async
import com.soywiz.korio.async.runBlockingNoJs
import eu.vendeli.tgbot.annotations.CtxProvider
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import io.github.jan.rediskm.core.RedisClient
import io.github.jan.rediskm.core.params.get.get
import io.github.jan.rediskm.core.params.misc.delete
import io.github.jan.rediskm.core.params.put.put
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers

@CtxProvider
open class ClassDataRedisImpl(
    host: String = "localhost",
    port: Int = 6379,
    user: String? = null,
    password: String? = null,
) : ClassData<String> {
    open val redis = RedisClient.create(host, port, password ?: "", user, true)

    override fun get(telegramId: Long, key: String): String? = runBlockingNoJs {
        redis.get<String>("classData-$telegramId-$key")
    }

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> =
        async(Dispatchers.Default) { redis.get<String>("classData-$telegramId-$key") }

    override fun del(telegramId: Long, key: String): Unit = runBlockingNoJs {
        redis.delete("classData-$telegramId-$key")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> =
        async(Dispatchers.Default) { redis.delete("classData-$telegramId-$key") > 0 }

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> =
        async(Dispatchers.Default) {
            redis.put<String>("classData-$telegramId-$key", value ?: "null").value == "OK"
        }

    override fun set(telegramId: Long, key: String, value: String?): Unit = runBlockingNoJs {
        redis.put<String>("classData-$telegramId-$key", value ?: "null")
    }

    override suspend fun clearAll(telegramId: Long) {
        redis.sendAndReceive("keys", "classData-$telegramId*")
            ?.value
            .fastCastTo<List<String>>()
            .map { it }
            .let {
                redis.delete(*it.toTypedArray())
            }
    }

    open suspend fun <T> set(telegramId: Long, key: String, value: T?) {
        redis.put("classData-$telegramId-$key", value ?: "null")
    }

    suspend inline fun <reified T> getVal(telegramId: Long, key: String) =
        redis.get<T>("classData-$telegramId-$key")
}
