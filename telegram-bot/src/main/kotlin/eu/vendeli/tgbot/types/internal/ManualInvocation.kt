package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits

internal data class ManualInvocation(
    val id: String,
    val invocation: suspend CommandContext.() -> Unit,
    val scope: Set<UpdateType>,
    val rateLimits: RateLimits,
)
