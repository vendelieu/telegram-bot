package eu.vendeli.tgbot.types.internal

class SingleInputChain(
    internal val id: String,
    internal val inputAction: suspend () -> Unit,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakCondition: (() -> Boolean)? = null,
)