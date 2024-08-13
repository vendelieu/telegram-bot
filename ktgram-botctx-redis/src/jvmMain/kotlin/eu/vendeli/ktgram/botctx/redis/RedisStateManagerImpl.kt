package eu.vendeli.ktgram.botctx.redis

import eu.vendeli.tgbot.interfaces.ctx.ChainStateManager
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.StoredState
import io.lettuce.core.RedisClient
import io.lettuce.core.api.reactive.RedisReactiveCommands
import kotlinx.serialization.json.Json

abstract class RedisStateManagerImpl(
    url: String = "redis://localhost",
) : ChainStateManager {
    open val redis: RedisReactiveCommands<String, String> = RedisClient.create(url).connect().reactive()
    private val storedStateSerde = StoredState.serializer()

    override suspend fun getState(user: User, link: String): StoredState? =
        redis.get("chainState-${user.id}-$link").block()?.let {
            Json.decodeFromString(storedStateSerde, it)
        }

    override suspend fun setState(user: User, link: String, value: StoredState) {
        redis.set("chainState-${user.id}-$link", Json.encodeToString(storedStateSerde, value))
    }

    override suspend fun clearAllState(user: User, chain: String) {
        redis.keys("chainState-${user.id}-$chain*").collectList().block()?.let {
            redis.del(*it.toTypedArray())
        }
    }
}
