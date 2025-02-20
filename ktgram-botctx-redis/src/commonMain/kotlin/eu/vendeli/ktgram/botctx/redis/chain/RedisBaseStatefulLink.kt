package eu.vendeli.ktgram.botctx.redis.chain

import eu.vendeli.tgbot.types.component.IdLong
import eu.vendeli.tgbot.types.chain.LinkStateManager
import eu.vendeli.tgbot.types.chain.StatefulLink
import eu.vendeli.tgbot.types.component.userOrNull
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

abstract class RedisBaseStatefulLink<L : StatefulLink<IdLong, String>>(
    host: String = "localhost",
    port: Int = 6379,
    linkRef: KClass<L>,
    serializer: Json,
) : StatefulLink<IdLong, String>() {
    override val state: LinkStateManager<IdLong, String> =
        BaseRedisLinkStateManager(host, port, String::class, linkRef, serializer) { it.userOrNull }
}
