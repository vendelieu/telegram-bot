package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.chain.LinkStateManager
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import eu.vendeli.tgbot.utils.fullName
import io.lettuce.core.RedisClient
import io.lettuce.core.api.reactive.RedisReactiveCommands
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass

abstract class RedisLinkStateManager<L : StatefulLink<T>, T : Any>(
    url: String = "redis://localhost",
    storageType: KClass<T>,
    private val linkRef: KClass<L>,
    private val serializer: Json = Json,
) : LinkStateManager<T> {
    open val redis: RedisReactiveCommands<String, String> = RedisClient.create(url).connect().reactive()

    @OptIn(InternalSerializationApi::class)
    private val storageTypeSerializer =
        storageType.serializerOrNull() ?: error("Serializer for $storageType is not found")

    override suspend fun get(user: User): T? =
        redis.get("linkState-${linkRef::class.fullName}-${user.id}").block()?.let {
            serializer.decodeFromString(storageTypeSerializer, it)
        }

    override suspend fun del(user: User) {
        redis.del("linkState-${linkRef::class.fullName}-${user.id}").block()
    }

    override suspend fun set(user: User, value: T) {
        val stringValue = serializer.encodeToString(storageTypeSerializer, value)
        redis.set("linkState-${linkRef::class.fullName}-${user.id}", stringValue).block()
    }
}
