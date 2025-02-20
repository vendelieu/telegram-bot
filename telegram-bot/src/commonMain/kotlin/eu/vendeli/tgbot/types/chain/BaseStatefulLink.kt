package eu.vendeli.tgbot.types.chain

import eu.vendeli.tgbot.types.component.IdLong
import eu.vendeli.tgbot.types.component.userOrNull

abstract class BaseStatefulLink : StatefulLink<IdLong, String>() {
    override val state: LinkStateManager<IdLong, String> = BaseLinkStateManager { it.userOrNull }
}
