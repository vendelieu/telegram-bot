package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

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
            element.jsonObject["date"]?.jsonPrimitive?.longOrNull == 0L -> InaccessibleMessage.serializer()
            else -> Message.serializer()
        }
    }
}
