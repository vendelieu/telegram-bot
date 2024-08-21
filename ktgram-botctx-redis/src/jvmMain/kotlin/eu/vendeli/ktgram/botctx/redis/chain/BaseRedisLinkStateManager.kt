package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.internal.IdLong
import eu.vendeli.tgbot.types.internal.chain.KeySelector
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

open class BaseRedisLinkStateManager<L : StatefulLink<IdLong, T>, T : Any>(
    url: String = "redis://localhost",
    storageType: KClass<T>,
    linkRef: KClass<L>,
    serializer: Json = Json,
    stateKeySelector: KeySelector<IdLong>,
) : RedisLinkStateManager<L, T>(url, storageType, linkRef, serializer, stateKeySelector)
