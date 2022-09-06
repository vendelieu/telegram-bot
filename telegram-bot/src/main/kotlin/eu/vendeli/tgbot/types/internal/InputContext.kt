package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User

data class InputContext(
    val user: User,
    val update: Update,
)
