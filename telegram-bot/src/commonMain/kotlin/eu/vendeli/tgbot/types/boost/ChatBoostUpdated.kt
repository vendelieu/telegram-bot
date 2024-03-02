package eu.vendeli.tgbot.types.boost

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * This object represents a boost added to a chat or changed.
 * @property chat Chat which was boosted
 * @property boost Information about the chat boost
 * Api reference: https://core.telegram.org/bots/api#chatboostupdated
*/
@Serializable
data class ChatBoostUpdated(
    val chat: Chat,
    val boost: ChatBoost,
)
