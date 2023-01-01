package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.Button

data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val callbackData: String? = null,
    val webApp: WebAppInfo? = null,
    val loginUrl: LoginUrl? = null,
    val switchInlineQuery: String? = null,
    val callbackGame: CallbackGame? = null,
    val pay: Boolean? = null,
) : Button
