package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import java.lang.reflect.Method

data class Invocation(
    val clazz: Class<*>,
    val method: Method,
    val namedParameters: Map<String, String> = mapOf(),
    val rateLimits: RateLimits,
)
