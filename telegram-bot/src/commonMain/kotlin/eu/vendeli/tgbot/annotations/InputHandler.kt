package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.implementations.DefaultGuard
import kotlin.reflect.KClass

/**
 * The annotation used to denote the function used to process the specified I/O event.
 *
 * @property value Keywords of the input.
 * @property rateLimits Query limits for this particular command.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InputHandler(
    val value: Array<String>,
    val rateLimits: RateLimits = RateLimits(0, 0),
    val guard: KClass<out Guard> = DefaultGuard::class,
)
