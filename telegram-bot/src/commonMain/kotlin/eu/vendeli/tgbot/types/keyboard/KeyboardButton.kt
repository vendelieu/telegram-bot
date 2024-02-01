package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.Button
import kotlinx.serialization.Serializable

@Serializable
data class KeyboardButton(
    val text: String,
    val requestUsers: KeyboardButtonRequestUsers? = null,
    val requestChat: KeyboardButtonRequestChat? = null,
    val requestContact: Boolean? = null,
    val requestLocation: Boolean? = null,
    val requestPoll: KeyboardButtonPollType? = null,
    val webApp: WebAppInfo? = null,
) : Button
