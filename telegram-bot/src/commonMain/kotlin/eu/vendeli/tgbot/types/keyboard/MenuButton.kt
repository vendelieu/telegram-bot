package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 * - MenuButtonCommands
 * - MenuButtonWebApp
 * - MenuButtonDefault
 * If a menu button other than MenuButtonDefault is set for a private chat, then it is applied in the chat. Otherwise the default menu button is applied. By default, the menu button opens the list of bot commands.
 *
 * [Api reference](https://core.telegram.org/bots/api#menubutton)
 *
 */
@Serializable
sealed class MenuButton {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("commands")
    class Commands : MenuButton()

    @Serializable
    @SerialName("web_app")
    data class WebApp(
        val text: String,
        val webApp: WebAppInfo,
    ) : MenuButton()

    @Serializable
    @SerialName("default")
    class Default : MenuButton()
}
