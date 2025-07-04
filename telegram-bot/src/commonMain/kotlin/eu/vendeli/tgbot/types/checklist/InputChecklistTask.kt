package eu.vendeli.tgbot.types.checklist

import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * Describes a task to add to a checklist.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputchecklisttask)
 * @property id Unique identifier of the task; must be positive and unique among all task identifiers currently present in the checklist
 * @property text Text of the task; 1-100 characters after entities parsing
 * @property parseMode Optional. Mode for parsing entities in the text. See formatting options for more details.
 * @property textEntities Optional. List of special entities that appear in the text, which can be specified instead of parse_mode. Currently, only bold, italic, underline, strikethrough, spoiler, and custom_emoji entities are allowed.
 */
@Serializable
data class InputChecklistTask(
    val id: Int,
    val text: String,
    val parseMode: String? = null,
    val textEntities: List<MessageEntity>? = null,
)
