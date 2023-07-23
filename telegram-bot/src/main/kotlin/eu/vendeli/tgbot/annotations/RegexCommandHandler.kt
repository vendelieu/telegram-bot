package eu.vendeli.tgbot.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegexCommandHandler(
    val value: String,
    val rateLimits: RateLimits = RateLimits(0, 0),
)
