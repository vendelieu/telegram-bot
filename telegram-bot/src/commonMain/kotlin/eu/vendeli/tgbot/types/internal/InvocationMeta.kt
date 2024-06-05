package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.interfaces.Filter
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlin.reflect.KClass

data class InvocationMeta(
    val qualifier: String,
    val function: String,
    val rateLimits: RateLimits,
    val guard: KClass<out Filter>,
) {
    override fun toString(): String = "$qualifier::$function $rateLimits | \uD83D\uDEE1\uFE0F ${guard.simpleName}"
}
