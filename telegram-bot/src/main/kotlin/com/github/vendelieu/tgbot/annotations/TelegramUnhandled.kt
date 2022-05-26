package com.github.vendelieu.tgbot.annotations

/**
 * Annotation used to mark the function that is used to handle unprocessed updates.
 * Only one processing point is possible.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelegramUnhandled
