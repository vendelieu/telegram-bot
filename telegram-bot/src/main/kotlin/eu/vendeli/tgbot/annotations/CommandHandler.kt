package eu.vendeli.tgbot.annotations

/**
 * The annotation used to mark the function that is used to process the specified commands.
 *
 * @property value Keywords of the command.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler(
    val value: Array<String>
)
