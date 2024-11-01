package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.rethis.ReThis
import eu.vendeli.rethis.wrappers.Hash
import eu.vendeli.tgbot.types.internal.IdLong
import eu.vendeli.tgbot.types.internal.chain.KeySelector
import eu.vendeli.tgbot.types.internal.chain.LinkStateManager
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import eu.vendeli.tgbot.utils.fqName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerOrNull
import kotlin.collections.set
import kotlin.reflect.KClass

abstract class RedisLinkStateManager<L, T>(
    host: String,
    port: Int,
    storageType: KClass<T>,
    private val linkRef: KClass<L>,
    private val serializer: Json = Json,
    stateSelector: KeySelector<IdLong>,
) : LinkStateManager<IdLong, T>
    where L : StatefulLink<IdLong, T>, T : Any {
    open val redis = ReThis(host, port)
    override val stateKey: KeySelector<IdLong> = stateSelector
    private val redisMap by lazy { redis.Hash("linkState-${linkRef::class.fqName}") }

    @OptIn(InternalSerializationApi::class)
    private val storageTypeSerializer =
        storageType.serializerOrNull() ?: error("Serializer for $storageType is not found")

    override suspend fun get(key: IdLong): T? = redisMap[key.id.toString()]?.let {
        serializer.decodeFromString(storageTypeSerializer, it)
    }

    override suspend fun del(key: IdLong) {
        redisMap.remove("${key.id}")
    }

    override suspend fun set(key: IdLong, value: T) {
        val stringValue = serializer.encodeToString(storageTypeSerializer, value)
        redisMap["${key.id}"] = stringValue
    }
}
