package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.types.component.UpdateType

/**
 * The annotation used to mark the function used to process the specified commands.
 *
 * @property value Keywords of the command.
 * @property scope Scope in which the command will be checked.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class CommandHandler(
    val value: Array<String>,
    val scope: Array<UpdateType> = [UpdateType.MESSAGE],
) {
    /**
     * Shortcut [CommandHandler] annotation with specified scope for [UpdateType.CALLBACK_QUERY].
     *
     * @property value Keywords of the command.
     * @property autoAnswer Reply to callbackQuery automatically (call `answerCallbackQuery` before handling).
     */
    annotation class CallbackQuery(
        val value: Array<String>,
        val autoAnswer: Boolean = false,
    )
}
