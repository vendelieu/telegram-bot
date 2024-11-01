package eu.vendeli.ktgram.botctx.redis.ctx

import eu.vendeli.rethis.ReThis
import eu.vendeli.rethis.commands.del
import eu.vendeli.rethis.commands.get
import eu.vendeli.rethis.commands.set
import eu.vendeli.tgbot.interfaces.ctx.UserData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

abstract class UserDataRedisImpl(
    host: String = "localhost",
    port: Int = 6379,
) : UserData<String> {
    open val redis = ReThis(host, port)

    override fun get(telegramId: Long, key: String): String? = runBlocking { redis.get("userData-$telegramId-$key") }

    override suspend fun getAsync(telegramId: Long, key: String): Deferred<String?> = coroutineScope {
        async {
            redis.get("userData-$telegramId-$key")
        }
    }

    override fun del(telegramId: Long, key: String): Unit = runBlocking {
        redis.del("userData-$telegramId-$key")
    }

    override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> = coroutineScope {
        async {
            redis.del("userData-$telegramId-$key") > 0
        }
    }

    override suspend fun setAsync(telegramId: Long, key: String, value: String?): Deferred<Boolean> = coroutineScope {
        async {
            redis.set("userData-$telegramId-$key", value ?: "null") == "OK"
        }
    }

    override fun set(telegramId: Long, key: String, value: String?): Unit = runBlocking {
        redis.set("userData-$telegramId-$key", value ?: "null")
    }
}
