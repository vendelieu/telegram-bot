package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
