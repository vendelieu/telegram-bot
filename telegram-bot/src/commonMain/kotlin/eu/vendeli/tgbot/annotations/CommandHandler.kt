package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.component.UpdateType
import kotlin.reflect.KClass

/**
 * The annotation used to mark the function used to process the specified commands.
 *
 * @property value Keywords of the command.
 * @property rateLimits Query limits for this particular command.
 * @property scope Scope in which the command will be checked.
 * @property guard Guard condition that will be checked.
 * @property argParser Custom argument parser.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CommandHandler(
    val value: Array<String>,
    @Deprecated(
        "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
    )
    val rateLimits: RateLimits = RateLimits(0, 0),
    val scope: Array<UpdateType> = [UpdateType.MESSAGE],
    @Deprecated(
        "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
    )
    val guard: KClass<out Guard> = DefaultGuard::class,
    @Deprecated(
        "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
    )
    val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
) {
    /**
     * Shortcut [CommandHandler] annotation with specified scope for [UpdateType.CALLBACK_QUERY].
     *
     * @property value Keywords of the command.
     * @property autoAnswer Reply to callbackQuery automatically (call `answerCallbackQuery` before handling).
     * @property rateLimits Query limits for this particular command.
     * @property guard Guard condition that will be checked.
     * @property argParser Custom argument parser.
     */
    annotation class CallbackQuery(
        val value: Array<String>,
        val autoAnswer: Boolean = false,
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val rateLimits: RateLimits = RateLimits(0, 0),
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val guard: KClass<out Guard> = DefaultGuard::class,
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
    )
}
