package eu.vendeli.tgbot.types.checklist

import eu.vendeli.tgbot.types.msg.Message
import kotlinx.serialization.Serializable

/**
 * Describes a service message about checklist tasks marked as done or not done.
 *
 * [Api reference](https://core.telegram.org/bots/api#checklisttasksdone)
 * @property checklistMessage Optional. Message containing the checklist whose tasks were marked as done or not done. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property markedAsDoneTaskIds Optional. Identifiers of the tasks that were marked as done
 * @property markedAsNotDoneTaskIds Optional. Identifiers of the tasks that were marked as not done
 */
@Serializable
data class ChecklistTasksDone(
    val checklistMessage: Message? = null,
    val markedAsDoneTaskIds: List<Int>? = null,
    val markedAsNotDoneTaskIds: List<Int>? = null,
)
