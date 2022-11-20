package eu.vendeli.tgbot.types.internal

class SingleInputChain(
    internal val id: String,
    internal val inputAction: suspend InputContext.() -> Unit,
    internal val rateLimits: RateLimits,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
