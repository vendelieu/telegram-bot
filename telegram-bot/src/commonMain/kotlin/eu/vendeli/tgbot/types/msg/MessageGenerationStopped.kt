package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * This object describes an update about a user stopping message generation.
 *
 * [Api reference](https://core.telegram.org/bots/api#messagegenerationstopped)
 * @property chat Chat in which the message is generated
 * @property messageThreadId Optional. Unique identifier of the message thread in which the message is generated
 * @property draftId Unique identifier of the message draft which was stopped
 */
@Serializable
data class MessageGenerationStopped(
    val chat: Chat,
    val messageThreadId: Int? = null,
    val draftId: Int,
)
