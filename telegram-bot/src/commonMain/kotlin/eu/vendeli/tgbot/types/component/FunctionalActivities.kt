package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.CommandActivities
import eu.vendeli.tgbot.utils.common.CommonActivities
import eu.vendeli.tgbot.utils.common.InputActivities
import eu.vendeli.tgbot.utils.common.OnUpdateActivities
import eu.vendeli.tgbot.utils.common.WhenNotHandledActivity

internal data class FunctionalActivities(
    val onUpdateActivities: OnUpdateActivities = mutableMapOf(),
    val inputs: InputActivities = mutableMapOf(),
    val commands: CommandActivities = mutableMapOf(),
    val commonActivities: CommonActivities = mutableMapOf(),
    var whenNotHandled: WhenNotHandledActivity? = null,
)
