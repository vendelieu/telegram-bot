package eu.vendeli.tgbot.types.chain

import eu.vendeli.tgbot.types.component.IdLong
import eu.vendeli.tgbot.types.component.userOrNull

@Deprecated("Use Wizard instead")
abstract class BaseStatefulLink : StatefulLink<IdLong, String>() {
    override val state: LinkStateManager<IdLong, String> = BaseLinkStateManager { it.userOrNull }
}
