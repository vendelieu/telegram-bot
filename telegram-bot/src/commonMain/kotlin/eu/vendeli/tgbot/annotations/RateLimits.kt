package eu.vendeli.tgbot.annotations

/**
 * Annotation to define query limits for specific actions.
 *
 * @property period The period for which the requests will be counted. (in milliseconds)
 * @property rate The number of requests for a certain period.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RateLimits(
    val period: Long = 0L,
    val rate: Long = 0L,
)
