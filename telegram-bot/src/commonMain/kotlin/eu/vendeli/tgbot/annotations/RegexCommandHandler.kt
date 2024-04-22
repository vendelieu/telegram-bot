package eu.vendeli.tgbot.annotations

/**
 * The annotation used to mark the function that is used to process the specified commands.
 *
 * @property value Regex of the command.
 * @property rateLimits Query limits for this particular command.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegexCommandHandler(
    val value: String,
    val rateLimits: RateLimits = RateLimits(0, 0),
    val options: Array<RegexOption> = [],
)
