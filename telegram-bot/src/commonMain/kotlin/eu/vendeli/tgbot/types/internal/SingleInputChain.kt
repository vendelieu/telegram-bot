package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnInputActivity

data class SingleInputChain(
    internal val id: String,
    internal val inputActivity: OnInputActivity,
    internal val rateLimits: RateLimits,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
