package eu.vendeli.tgbot.annotations

/**
 * Annotation to define query limits for specific actions.
 *
 * @property period The period for which the requests will be counted
 * @property rate The number of requests for a certain period. (in milliseconds)
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RateLimits(
    val period: Long = 1000L,
    val rate: Long = 20L
)
