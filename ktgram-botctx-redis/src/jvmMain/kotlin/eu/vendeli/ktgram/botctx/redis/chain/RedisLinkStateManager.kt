package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.internal.IdLong
import eu.vendeli.tgbot.types.internal.chain.KeySelector
import eu.vendeli.tgbot.types.internal.chain.LinkStateManager
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import eu.vendeli.tgbot.utils.fullName
import io.lettuce.core.RedisClient
import io.lettuce.core.api.reactive.RedisReactiveCommands
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass

abstract class RedisLinkStateManager<L, T>(
    url: String = "redis://localhost",
    storageType: KClass<T>,
    private val linkRef: KClass<L>,
    private val serializer: Json = Json,
    stateSelector: KeySelector<IdLong>,
) : LinkStateManager<IdLong, T>
    where L : StatefulLink<IdLong, T>, T : Any {
    open val redis: RedisReactiveCommands<String, String> = RedisClient.create(url).connect().reactive()
    override val stateKey: KeySelector<IdLong> = stateSelector

    @OptIn(InternalSerializationApi::class)
    private val storageTypeSerializer =
        storageType.serializerOrNull() ?: error("Serializer for $storageType is not found")

    override suspend fun get(entity: IdLong): T? =
        redis.get("linkState-${linkRef::class.fullName}-${entity.id}").block()?.let {
            serializer.decodeFromString(storageTypeSerializer, it)
        }

    override suspend fun del(entity: IdLong) {
        redis.del("linkState-${linkRef::class.fullName}-${entity.id}").block()
    }

    override suspend fun set(entity: IdLong, value: T) {
        val stringValue = serializer.encodeToString(storageTypeSerializer, value)
        redis.set("linkState-${linkRef::class.fullName}-${entity.id}", stringValue).block()
    }
}
