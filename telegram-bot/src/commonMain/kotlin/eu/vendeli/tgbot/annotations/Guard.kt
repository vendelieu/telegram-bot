package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.Guard
import kotlin.reflect.KClass

/**
 * The annotation used to set a guarding mechanism that handlers will pick.
 *
 * Supported by [CommandHandler], [CommandHandler.CallbackQuery], [InputHandler].
 *
 * @property guard Guard condition that will be checked.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
annotation class Guard(
    val guard: KClass<out Guard> = DefaultGuard::class,
)
