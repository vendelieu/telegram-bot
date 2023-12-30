package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import java.time.Instant

sealed class MaybeInaccessibleMessage {
    data class InaccessibleMessage(
        val chat: Chat,
        val messageId: Long,
        val date: Instant,
    ) : MaybeInaccessibleMessage()
}
