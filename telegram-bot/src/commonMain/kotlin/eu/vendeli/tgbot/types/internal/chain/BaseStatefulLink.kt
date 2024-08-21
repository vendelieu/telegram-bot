package eu.vendeli.tgbot.types.internal.chain

import eu.vendeli.tgbot.types.internal.IdLong
import eu.vendeli.tgbot.types.internal.userOrNull

abstract class BaseStatefulLink : StatefulLink<IdLong, String>() {
    override val state: LinkStateManager<IdLong, String> = BaseLinkStateManager { it.userOrNull }
}
