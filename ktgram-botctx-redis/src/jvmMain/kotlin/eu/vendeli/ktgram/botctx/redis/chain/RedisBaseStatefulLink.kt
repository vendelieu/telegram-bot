package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.internal.chain.LinkStateManager
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

abstract class RedisBaseStatefulLink<L : StatefulLink<String>>(
    url: String = "redis://localhost",
    linkRef: KClass<L>,
    serializer: Json,
) : StatefulLink<String>() {
    override val state: LinkStateManager<String> = BaseRedisLinkStateManager(url, String::class, linkRef, serializer)
}
