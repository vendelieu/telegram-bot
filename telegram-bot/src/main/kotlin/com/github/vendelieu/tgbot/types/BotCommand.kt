package com.github.vendelieu.tgbot.types

import com.github.vendelieu.tgbot.interfaces.MultipleResponse

data class BotCommand(
    val command: String,
    val description: String,
) : MultipleResponse
