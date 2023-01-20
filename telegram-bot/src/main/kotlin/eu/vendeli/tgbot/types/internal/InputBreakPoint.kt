package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.OnInputAction

data class InputBreakPoint(
    val condition: InputContext.() -> Boolean,
    val action: OnInputAction? = null,
    val repeat: Boolean = true,
)
