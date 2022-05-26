package com.github.vendelieu.tgbot.types

sealed class MenuButton(open val type: String) {
    object Commands : MenuButton(type = "commands")
    data class WebApps(val text: String, val webApp: WebAppInfo) : MenuButton(type = "web_apps")
    object Default : MenuButton(type = "default")
}
