package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.User

data class CommandContext(
    val parameters: Map<String, String>,
    val user: User
)
