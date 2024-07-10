package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.utils.CommandHandlers
import eu.vendeli.tgbot.utils.CommonHandlers
import eu.vendeli.tgbot.utils.InputHandlers
import eu.vendeli.tgbot.utils.InvocationLambda
import eu.vendeli.tgbot.utils.UpdateTypeHandlers
import eu.vendeli.tgbot.utils._OperatingActivities
import eu.vendeli.tgbot.utils.logString

@Suppress("UNCHECKED_CAST")
internal class ActivitiesData(
    pkg: String? = null,
) {
    @OptIn(InternalApi::class)
    private val activities = when {
        _OperatingActivities.size == 1 -> _OperatingActivities.entries.firstOrNull()?.value
        _OperatingActivities.size > 1 && pkg != null -> _OperatingActivities[pkg]
        else -> notFoundGeneratedActivities()
    } ?: notFoundGeneratedActivities()

    val commandHandlers = activities[0] as CommandHandlers
    val inputHandlers = activities[1] as InputHandlers
    val commonHandlers = activities[2] as CommonHandlers
    val updateTypeHandlers = activities[3] as UpdateTypeHandlers
    val unprocessedHandler = activities[4] as InvocationLambda?

    init {
        TgUpdateHandler.logger.info {
            "\nCommandHandlers:\n${commandHandlers.logString}\n" +
                "InputHandlers:\n${inputHandlers.logString}\n" +
                "CommonHandlers:\n${commonHandlers.logString}\n" +
                "UpdateTypeHandlers:\n${updateTypeHandlers.logString}\n" +
                "UnprocessedHandler:\n${unprocessedHandler ?: "None"}"
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun notFoundGeneratedActivities(): Nothing =
        error("Not found generated actions, check if ksp plugin and ksp processor is connected correctly.")
}
