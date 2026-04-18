package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.component.UpdateType

/**
 * An annotation used to indicate the function that is used to handle the specified type of incoming update.
 *
 * @property type Type of update.
 * @property messageKind Optional set of [MessageKind]s that narrow dispatch to message-bearing updates
 * whose detected kind matches. Empty (default) means any kind.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UpdateHandler(
    val type: Array<UpdateType>,
    val messageKind: Array<MessageKind> = [],
)
