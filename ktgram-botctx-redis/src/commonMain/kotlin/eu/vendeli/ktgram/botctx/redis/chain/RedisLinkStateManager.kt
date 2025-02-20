package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.rethis.ReThis
import eu.vendeli.rethis.wrappers.Hash
import eu.vendeli.tgbot.types.component.IdLong
import eu.vendeli.tgbot.types.chain.KeySelector
import eu.vendeli.tgbot.types.chain.LinkStateManager
import eu.vendeli.tgbot.types.chain.StatefulLink
import eu.vendeli.tgbot.utils.common.TgException
import eu.vendeli.tgbot.utils.common.fqName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerOrNull
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
        storageType.serializerOrNull() ?: throw TgException("Serializer for $storageType is not found")

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
