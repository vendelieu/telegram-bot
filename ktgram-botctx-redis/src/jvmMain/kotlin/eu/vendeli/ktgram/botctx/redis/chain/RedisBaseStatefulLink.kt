package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.internal.IdLong
import eu.vendeli.tgbot.types.internal.chain.LinkStateManager
import eu.vendeli.tgbot.types.internal.chain.StatefulLink
import eu.vendeli.tgbot.types.internal.userOrNull
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

abstract class RedisBaseStatefulLink<L : StatefulLink<IdLong, String>>(
    url: String = "redis://localhost",
    linkRef: KClass<L>,
    serializer: Json,
) : StatefulLink<IdLong, String>() {
    override val state: LinkStateManager<IdLong, String> = BaseRedisLinkStateManager(url, String::class, linkRef, serializer) { it.userOrNull }
}
