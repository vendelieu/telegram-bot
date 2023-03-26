package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.types.internal.CommandScope

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
    val scope: Array<CommandScope> = [CommandScope.MESSAGE, CommandScope.CALLBACK],
)
