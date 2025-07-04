package eu.vendeli.tgbot.types.checklist

import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * Describes a checklist to create.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputchecklist)
 * @property title Title of the checklist; 1-255 characters after entities parsing
 * @property parseMode Optional. Mode for parsing entities in the title. See formatting options for more details.
 * @property titleEntities Optional. List of special entities that appear in the title, which can be specified instead of parse_mode. Currently, only bold, italic, underline, strikethrough, spoiler, and custom_emoji entities are allowed.
 * @property tasks List of 1-30 tasks in the checklist
 * @property othersCanAddTasks Optional. Pass True if other users can add tasks to the checklist
 * @property othersCanMarkTasksAsDone Optional. Pass True if other users can mark tasks as done or not done in the checklist
 */
@Serializable
data class InputChecklist(
    val title: String,
    val parseMode: String? = null,
    val titleEntities: List<MessageEntity>? = null,
    val tasks: List<InputChecklistTask>,
    val othersCanAddTasks: Boolean? = null,
    val othersCanMarkTasksAsDone: Boolean? = null,
)
