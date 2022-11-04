package eu.vendeli.tgbot.annotations

/**
 * Annotation used to mark the function that is used to handle updates that not processed.
 * Multiple processing point is possible.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UnprocessedHandler
