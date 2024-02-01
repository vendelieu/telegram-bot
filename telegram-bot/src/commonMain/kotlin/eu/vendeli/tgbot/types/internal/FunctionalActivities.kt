package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.CommandActivities
import eu.vendeli.tgbot.utils.InputActivities
import eu.vendeli.tgbot.utils.OnUpdateActivities
import eu.vendeli.tgbot.utils.RegexCommandActivities
import eu.vendeli.tgbot.utils.WhenNotHandledActivity

internal data class FunctionalActivities(
    val onUpdateActivities: OnUpdateActivities = mutableMapOf(),
    val inputs: InputActivities = mutableMapOf(),
    val commands: CommandActivities = mutableMapOf(),
    val regexCommands: RegexCommandActivities = mutableMapOf(),
    var whenNotHandled: WhenNotHandledActivity? = null,
)
