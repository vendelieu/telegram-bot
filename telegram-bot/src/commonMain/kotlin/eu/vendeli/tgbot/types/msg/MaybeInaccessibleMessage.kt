package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import eu.vendeli.tgbot.utils.serde.primitiveOrNull
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull

/**
 * This object describes a message that can be inaccessible to the bot. It can be one of
 * - Message
 * - InaccessibleMessage
 *
 * [Api reference](https://core.telegram.org/bots/api#maybeinaccessiblemessage)
 *
 */
@Serializable(MaybeInaccessibleMessage.Companion::class)
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

    internal companion object :
        JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {
        override fun selectDeserializer(element: JsonElement) = when {
            element.jsonObject["date"]?.primitiveOrNull?.longOrNull == 0L -> InaccessibleMessage.serializer()
            else -> Message.serializer()
        }
    }
}
