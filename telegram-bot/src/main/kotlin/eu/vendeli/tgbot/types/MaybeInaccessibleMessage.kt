package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import eu.vendeli.tgbot.utils.serde.MaybeInaccessibleMessageSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable(MaybeInaccessibleMessageSerializer::class)
sealed class MaybeInaccessibleMessage {
    abstract val chat: Chat
    abstract val messageId: Long

    @Serializable(InstantSerializer::class)
    abstract val date: Instant

    @Serializable
    data class InaccessibleMessage(
        override val chat: Chat,
        override val messageId: Long,
        @Serializable(InstantSerializer::class)
        override val date: Instant,
    ) : MaybeInaccessibleMessage()
}
