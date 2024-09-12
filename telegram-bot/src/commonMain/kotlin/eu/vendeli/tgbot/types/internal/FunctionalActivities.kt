package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.CommandActivities
import eu.vendeli.tgbot.utils.CommonActivities
import eu.vendeli.tgbot.utils.InputActivities
import eu.vendeli.tgbot.utils.OnUpdateActivities
import eu.vendeli.tgbot.utils.WhenNotHandledActivity

internal data class FunctionalActivities(
    val onUpdateActivities: OnUpdateActivities = mutableMapOf(),
    val inputs: InputActivities = mutableMapOf(),
    val commands: CommandActivities = mutableMapOf(),
    val commonActivities: CommonActivities = mutableMapOf(),
    var whenNotHandled: WhenNotHandledActivity? = null,
)
