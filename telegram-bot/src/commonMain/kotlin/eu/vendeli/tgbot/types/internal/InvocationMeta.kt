package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits

data class InvocationMeta(
    val qualifier: String,
    val function: String,
    val rateLimits: RateLimits,
)
