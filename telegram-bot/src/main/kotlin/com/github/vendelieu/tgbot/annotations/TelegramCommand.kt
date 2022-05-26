package com.github.vendelieu.tgbot.annotations

/**
 * The annotation used to mark the function that is used to process the specified commands.
 *
 * @property value Keywords of the command.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelegramCommand(
    val value: Array<String>
)
