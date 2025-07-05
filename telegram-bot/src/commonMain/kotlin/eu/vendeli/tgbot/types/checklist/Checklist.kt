package eu.vendeli.tgbot.types.checklist

import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * Describes a checklist.
 *
 * [Api reference](https://core.telegram.org/bots/api#checklist)
 * @property title Title of the checklist
 * @property titleEntities Optional. Special entities that appear in the checklist title
 * @property tasks List of tasks in the checklist
 * @property othersCanAddTasks Optional. True, if users other than the creator of the list can add tasks to the list
 * @property othersCanMarkTasksAsDone Optional. True, if users other than the creator of the list can mark tasks as done or not done
 */
@Serializable
data class Checklist(
    val title: String,
    val titleEntities: List<MessageEntity>? = null,
    val tasks: List<ChecklistTask>,
    val othersCanAddTasks: Boolean? = null,
    val othersCanMarkTasksAsDone: Boolean? = null,
)
