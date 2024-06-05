package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.interfaces.Filter
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnInputActivity
import kotlin.reflect.KClass

data class SingleInputChain(
    internal val id: String,
    internal val inputActivity: OnInputActivity,
    internal val rateLimits: RateLimits,
    internal val guard: KClass<out Filter>,
    internal val currentLevel: Int = 0,
    internal var tail: String? = null,
    internal var breakPoint: InputBreakPoint? = null,
)
