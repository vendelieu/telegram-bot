package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.interfaces.Guard
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.DefaultGuard
import kotlin.reflect.KClass

data class InvocationMeta(
    val qualifier: String,
    val function: String,
    val rateLimits: RateLimits,
    val guard: KClass<out Guard> = DefaultGuard::class,
) {
    override fun toString(): String = "$qualifier::$function $rateLimits | \uD83D\uDEE1\uFE0F ${guard.simpleName}"
}
