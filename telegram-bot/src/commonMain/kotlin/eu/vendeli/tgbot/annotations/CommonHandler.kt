package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.interfaces.Filter
import eu.vendeli.tgbot.utils.DefaultFilter
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CommonHandler {
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Text(
        val value: Array<String>,
        val filter: KClass<out Filter> = DefaultFilter::class,
        val priority: Int = 0,
        val rateLimits: RateLimits = RateLimits(0, 0),
    )

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Regex(
        val value: String,
        val options: Array<RegexOption> = [],
        val filter: KClass<out Filter> = DefaultFilter::class,
        val priority: Int = 0,
        val rateLimits: RateLimits = RateLimits(0, 0),
    )
}
