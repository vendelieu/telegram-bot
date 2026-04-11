package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 *
 * [Api reference](https://core.telegram.org/bots/api#polloption)
 * @property persistentId Unique identifier of the option, persistent on option addition and deletion
 * @property text Option text, 1-100 characters
 * @property textEntities Optional. Special entities that appear in the option text. Currently, only custom emoji entities are allowed in poll option texts
 * @property voterCount Number of users who voted for this option; may be 0 if unknown
 * @property addedByUser Optional. User who added the option; omitted if the option wasn't added by a user after poll creation
 * @property addedByChat Optional. Chat that added the option; omitted if the option wasn't added by a chat after poll creation
 * @property additionDate Optional. Point in time (Unix timestamp) when the option was added; omitted if the option existed in the original poll
 */
@Serializable
data class PollOption(
    val persistentId: String,
    val text: String,
    val voterCount: Int,
    val textEntities: List<MessageEntity>? = null,
    val addedByUser: User? = null,
    val addedByChat: Chat? = null,
    @Serializable(InstantSerializer::class)
    val additionDate: Instant? = null,
)
