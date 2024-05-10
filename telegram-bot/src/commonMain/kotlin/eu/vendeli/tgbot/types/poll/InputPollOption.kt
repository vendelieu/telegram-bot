package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.MessageEntity
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll to send.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputpolloption)
 * @property text Option text, 1-100 characters
 * @property textParseMode Optional. Mode for parsing entities in the text. See formatting options for more details. Currently, only custom emoji entities are allowed
 * @property textEntities Optional. A JSON-serialized list of special entities that appear in the poll option text. It can be specified instead of text_parse_mode
 */
@Serializable
data class InputPollOption(
    val text: String,
    val textParseMode: String? = null,
    val textEntities: List<MessageEntity>? = null,
)
