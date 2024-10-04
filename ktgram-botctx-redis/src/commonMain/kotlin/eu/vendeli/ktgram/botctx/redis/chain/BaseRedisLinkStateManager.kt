package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.internal.IdLong
import eu.vendeli.tgbot.types.internal.chain.KeySelector
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

open class BaseRedisLinkStateManager<L : StatefulLink<IdLong, T>, T : Any>(
    host: String = "localhost",
    port: Int = 6379,
    storageType: KClass<T>,
    linkRef: KClass<L>,
    serializer: Json = Json,
    stateKeySelector: KeySelector<IdLong>,
) : RedisLinkStateManager<L, T>(host, port, storageType, linkRef, serializer, stateKeySelector)
