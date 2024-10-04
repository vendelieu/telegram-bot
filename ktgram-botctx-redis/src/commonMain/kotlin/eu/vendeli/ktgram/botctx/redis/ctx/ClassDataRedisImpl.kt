package eu.vendeli.ktgram.botctx.redis.ctx

import eu.vendeli.rethis.ReThis
import eu.vendeli.rethis.commands.del
import eu.vendeli.rethis.commands.get
import eu.vendeli.rethis.commands.keys
import eu.vendeli.rethis.commands.set
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

abstract class ClassDataRedisImpl(
    host: String = "localhost",
    port: Int = 6379,
) : ClassData<String> {
    open val redis = ReThis(host, port)

    override fun get(telegramId: Long, key: String): String? = runBlocking { redis.get("classData-$telegramId-$key") }

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> = coroutineScope {
        async {
            redis.get("classData-$telegramId-$key")
        }
    }

    override fun del(telegramId: Long, key: String): Unit = runBlocking {
        redis.del("classData-$telegramId-$key")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = coroutineScope {
        async {
            redis.del("classData-$telegramId-$key") > 0
        }
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> = coroutineScope {
        async {
            redis.set("classData-$telegramId-$key", value ?: "null") == "OK"
        }
    }

    override suspend fun clearAll(telegramId: Long) {
        redis.keys("classData-$telegramId*").let {
            redis.del(*it.toTypedArray())
        }
    }

    override fun set(telegramId: Long, key: String, value: String?): Unit = runBlocking {
        redis.set("classData-$telegramId-$key", value ?: "null")
    }
}
