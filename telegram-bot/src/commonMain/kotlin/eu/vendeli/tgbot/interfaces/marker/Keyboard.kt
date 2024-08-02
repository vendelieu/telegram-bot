package eu.vendeli.tgbot.interfaces.marker

import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.ReplyKeyboardMarkup
import eu.vendeli.tgbot.utils.serde.KeyboardSerializer
import kotlinx.serialization.Serializable

/**
 * Marker interface of Keyboards, like [ReplyKeyboardMarkup], [InlineKeyboardMarkup] etc.
 */
@Serializable(KeyboardSerializer::class)
interface Keyboard
