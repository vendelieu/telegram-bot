package eu.vendeli.tgbot.utils.serde

import eu.vendeli.tgbot.interfaces.marker.Keyboard
import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardRemove
import eu.vendeli.tgbot.utils.TgException
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

internal object KeyboardSerializer : JsonContentPolymorphicSerializer<Keyboard>(Keyboard::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Keyboard> {
        val content = element.jsonObject
        return when {
            content.contains("inline_keyboard") -> InlineKeyboardMarkup.serializer()
            content.contains("keyboard") -> ReplyKeyboardMarkup.serializer()
            content.contains("remove_keyboard") -> ReplyKeyboardRemove.serializer()
            content.contains("force_reply") -> ForceReply.serializer()
            else -> throw TgException("Unsupported keyboard - $content")
        }
    }
}
