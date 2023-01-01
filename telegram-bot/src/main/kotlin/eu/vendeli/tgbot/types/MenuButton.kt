package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = MenuButton.Default::class,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MenuButton.Commands::class, name = "commands"),
    JsonSubTypes.Type(value = MenuButton.WebApps::class, name = "web_apps"),
    JsonSubTypes.Type(value = MenuButton.Default::class, name = "default"),
)
sealed class MenuButton(open val type: String) {
    object Commands : MenuButton(type = "commands")
    data class WebApps(val text: String, val webApp: WebAppInfo) : MenuButton(type = "web_apps")
    object Default : MenuButton(type = "default")
}
