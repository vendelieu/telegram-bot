package eu.vendeli.tgbot.types.chain

import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.component.InputBreakPoint
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.OnInputActivity

data class SingleInputChain(
    internal val id: String,
    internal val inputActivity: OnInputActivity,
    internal val rateLimits: RateLimits,
    internal val guard: Guard,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
