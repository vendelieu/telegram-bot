package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnInputAction

class SingleInputChain(
    internal val id: String,
    internal val inputAction: OnInputAction,
    internal val rateLimits: RateLimits,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
