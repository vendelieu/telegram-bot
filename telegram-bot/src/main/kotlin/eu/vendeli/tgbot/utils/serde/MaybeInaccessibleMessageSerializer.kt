package eu.vendeli.tgbot.utils.serde

import eu.vendeli.tgbot.types.MaybeInaccessibleMessage
import eu.vendeli.tgbot.types.Message
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

internal object MaybeInaccessibleMessageSerializer :
    JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        element.jsonObject["date"]?.jsonPrimitive?.longOrNull == 0L
        -> MaybeInaccessibleMessage.InaccessibleMessage.serializer()

        else -> Message.serializer()
    }
}
