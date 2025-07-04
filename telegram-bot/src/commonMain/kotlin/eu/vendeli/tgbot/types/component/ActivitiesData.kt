package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.CommandHandlers
import eu.vendeli.tgbot.utils.common.CommonHandlers
import eu.vendeli.tgbot.utils.common.InputHandlers
import eu.vendeli.tgbot.utils.common.InvocationLambda
import eu.vendeli.tgbot.utils.common.TgException
import eu.vendeli.tgbot.utils.common.UpdateTypeHandlers
import eu.vendeli.tgbot.utils.common._OperatingActivities
import eu.vendeli.tgbot.utils.internal.info
import io.ktor.util.logging.Logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
internal class ActivitiesData(
    pkg: String? = null,
    logger: Logger,
) {
    private val activities = when {
        _OperatingActivities.size == 1 -> _OperatingActivities.entries.firstOrNull()?.value
        _OperatingActivities.size > 1 && pkg != null -> _OperatingActivities[pkg]
        else -> generatedActivitiesNotFound()
    } ?: generatedActivitiesNotFound()

    val commandHandlers = activities[0] as CommandHandlers
    val inputHandlers = activities[1] as InputHandlers
    val commonHandlers = activities[2] as CommonHandlers
    val updateTypeHandlers = activities[3] as UpdateTypeHandlers
    val unprocessedHandler = activities[4] as InvocationLambda?

    init {
        @Suppress("OPT_IN_USAGE")
        GlobalScope.launch {
            logger.info {
                "\nCommandHandlers:\n${commandHandlers.logString}\n\n" +
                    "InputHandlers:\n${inputHandlers.logString}\n\n" +
                    "CommonHandlers:\n${commonHandlers.logString}\n\n" +
                    "UpdateTypeHandlers:\n${updateTypeHandlers.logString}\n\n" +
                    "UnprocessedHandler:\n${unprocessedHandler ?: "None"}"
            }
        }
    }

    private inline fun generatedActivitiesNotFound(): Nothing = throw TgException(
        "Generated activities not found, check if ksp plugin and ksp processor is connected correctly.",
    )

    private inline val <K, V : Any> Map<K, V>.logString: String
        get() = takeIf { isNotEmpty() }?.entries?.joinToString(",\n") {
            "${it.key} - " + if (it.value is Pair<*, *>) {
                (it.value as Pair<*, *>).second
            } else {
                it.value
            }.toString()
        } ?: "None"
}
