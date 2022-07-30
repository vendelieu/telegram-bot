package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update

data class InputBreakPoint(
    val condition: Update.() -> Boolean,
    val action: (suspend Update.() -> Unit)? = null,
)
