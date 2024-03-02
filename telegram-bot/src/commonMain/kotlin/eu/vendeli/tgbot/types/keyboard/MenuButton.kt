package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 * - MenuButtonCommands
 * - MenuButtonWebApp
 * - MenuButtonDefault
 * If a menu button other than MenuButtonDefault is set for a private chat, then it is applied in the chat. Otherwise the default menu button is applied. By default, the menu button opens the list of bot commands.
 *
 * Api reference: https://core.telegram.org/bots/api#menubutton
*/
@Serializable
sealed class MenuButton(open val type: String) {
    @Serializable
    @SerialName("commands")
    class Commands : MenuButton(type = "commands")

    @Serializable
    @SerialName("web_apps")
    data class WebApps(val text: String, val webApp: WebAppInfo) : MenuButton(type = "web_apps")

    @Serializable
    @SerialName("default")
    class Default : MenuButton(type = "default")
}
