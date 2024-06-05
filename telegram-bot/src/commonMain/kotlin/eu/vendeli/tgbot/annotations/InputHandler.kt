package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.interfaces.Filter
import eu.vendeli.tgbot.utils.DefaultFilter
import kotlin.reflect.KClass

/**
 * The annotation used to denote the function that is used to process the specified I/O event.
 *
 * @property value Keywords of the input.
 * @property rateLimits Query limits for this particular command.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InputHandler(
    val value: Array<String>,
    val rateLimits: RateLimits = RateLimits(0, 0),
    val guard: KClass<out Filter> = DefaultFilter::class
)
