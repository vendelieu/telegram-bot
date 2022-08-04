package eu.vendeli.tgbot.types.internal

data class InputBreakPoint(
    val condition: InputContext.() -> Boolean,
    val action: (suspend InputContext.() -> Unit)? = null,
)
