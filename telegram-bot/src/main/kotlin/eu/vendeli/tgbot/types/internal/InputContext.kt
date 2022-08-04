package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.User

data class InputContext(
    val user: User,
    val message: Message,
)