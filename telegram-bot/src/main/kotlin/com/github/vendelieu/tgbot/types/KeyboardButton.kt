package com.github.vendelieu.tgbot.types

data class KeyboardButton(
    val text: String,
    val requestContact: Boolean? = null,
    val requestLocation: Boolean? = null,
    val requestPoll: KeyboardButtonPollType? = null,
    val webApp: WebAppInfo? = null,
)
