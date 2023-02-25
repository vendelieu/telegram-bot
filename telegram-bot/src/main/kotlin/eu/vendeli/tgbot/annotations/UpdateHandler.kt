package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.types.internal.UpdateType

/**
 * An annotation used to indicate the function that is used to handle the specified type of incoming update.
 *
 * @property type Type of update.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class UpdateHandler(
    val type: UpdateType,
)
