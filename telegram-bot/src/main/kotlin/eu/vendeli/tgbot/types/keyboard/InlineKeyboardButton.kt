package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.Button
import eu.vendeli.tgbot.types.game.CallbackGame
import eu.vendeli.tgbot.types.inline.SwitchInlineQueryChosenChat
import kotlinx.serialization.Serializable

@Serializable
data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val callbackData: String? = null,
    val webApp: WebAppInfo? = null,
    val loginUrl: LoginUrl? = null,
    val switchInlineQuery: String? = null,
    val switchInlineQueryCurrentChat: String? = null,
    val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null,
    val callbackGame: CallbackGame? = null,
    val pay: Boolean? = null,
) : Button
