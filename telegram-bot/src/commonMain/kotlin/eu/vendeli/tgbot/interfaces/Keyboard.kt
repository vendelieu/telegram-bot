package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardRemove
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Marker interface of Keyboards, like [ReplyKeyboardMarkup], [InlineKeyboardMarkup] etc.
 */
@Serializable(Keyboard.Companion::class)
interface Keyboard {
    companion object : JsonContentPolymorphicSerializer<Keyboard>(Keyboard::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Keyboard> {
            val content = element.jsonObject
            return when {
                content.contains("inline_keyboard") -> InlineKeyboardMarkup.serializer()
                content.contains("keyboard") -> ReplyKeyboardMarkup.serializer()
                content.contains("remove_keyboard") -> ReplyKeyboardRemove.serializer()
                content.contains("force_reply") -> ForceReply.serializer()
                else -> error("Unsupported keyboard - $content")
            }
        }
    }
}
