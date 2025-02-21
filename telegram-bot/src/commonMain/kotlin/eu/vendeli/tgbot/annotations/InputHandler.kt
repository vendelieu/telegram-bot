package eu.vendeli.tgbot.annotations

/**
 * The annotation used to denote the function used to process the specified I/O event.
 *
 * @property value Keywords of the input.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class InputHandler(
    val value: Array<String>,
)
