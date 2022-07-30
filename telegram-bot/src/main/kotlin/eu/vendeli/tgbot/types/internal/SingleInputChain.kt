package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.Update

class SingleInputChain(
    internal val id: String,
    internal val inputAction: suspend Update.() -> Unit,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
