package eu.vendeli.tgbot.types.checklist

import eu.vendeli.tgbot.types.msg.Message
import kotlinx.serialization.Serializable

/**
 * Describes a service message about tasks added to a checklist.
 *
 * [Api reference](https://core.telegram.org/bots/api#checklisttasksadded)
 * @property checklistMessage Optional. Message containing the checklist to which the tasks were added. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property tasks List of tasks added to the checklist
 */
@Serializable
data class ChecklistTasksAdded(
    val checklistMessage: Message? = null,
    val tasks: List<ChecklistTask>,
)
