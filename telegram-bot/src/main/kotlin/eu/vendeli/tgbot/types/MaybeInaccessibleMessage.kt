package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import java.time.Instant

sealed class MaybeInaccessibleMessage {
    abstract val chat: Chat
    abstract val messageId: Long
    abstract val date: Instant

    data class InaccessibleMessage(
        override val chat: Chat,
        override val messageId: Long,
        override val date: Instant,
    ) : MaybeInaccessibleMessage()
}
