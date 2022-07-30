package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update

data class ActionContext<T>(
    val update: Update,
    val data: T,
)
