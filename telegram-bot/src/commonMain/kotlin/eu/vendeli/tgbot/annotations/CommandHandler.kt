package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.types.internal.UpdateType

/**
 * The annotation used to mark the function that is used to process the specified commands.
 *
 * @property value Keywords of the command.
 * @property rateLimits Query limits for this particular command.
 * @property scope Scope in which the command will be checked.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler(
    val value: Array<String>,
    val rateLimits: RateLimits = RateLimits(0, 0),
    val scope: Array<UpdateType> = [UpdateType.MESSAGE],
) {
    /**
     * Shortcut [CommandHandler] annotation with specified scope for [UpdateType.CALLBACK_QUERY].
     *
     * @property value Keywords of the command.
     * @property rateLimits Query limits for this particular command.
     */
    annotation class CallbackQuery(
        val value: Array<String>,
        val rateLimits: RateLimits = RateLimits(0, 0),
    )
}
