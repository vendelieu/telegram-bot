package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User

data class CommandContext(
    val update: Update,
    val parameters: Map<String, String>,
    val user: User,
)
