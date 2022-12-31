package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User

data class ProcessedUpdate(
    val type: UpdateType,
    val text: String? = null,
    val user: User,
    val fullUpdate: Update,
)
