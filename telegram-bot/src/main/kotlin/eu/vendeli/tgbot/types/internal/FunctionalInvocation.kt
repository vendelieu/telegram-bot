package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnCommandActivity

internal data class FunctionalInvocation(
    val id: String,
    val invocation: OnCommandActivity,
    val scope: Set<UpdateType>,
    val rateLimits: RateLimits,
)
