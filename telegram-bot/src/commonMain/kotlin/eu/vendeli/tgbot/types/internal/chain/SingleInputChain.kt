package eu.vendeli.tgbot.types.internal.chain

import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.internal.InputBreakPoint
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnInputActivity

data class SingleInputChain(
    internal val id: String,
    internal val inputActivity: OnInputActivity,
    internal val rateLimits: RateLimits,
    internal val guard: Guard,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
