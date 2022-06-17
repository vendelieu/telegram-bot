package eu.vendeli.tgbot.annotations

/**
 * The annotation used to denote the function that is used to process the specified I/O event.
 *
 * @property value Keywords of the input.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelegramInput(
    val value: Array<String>
)
