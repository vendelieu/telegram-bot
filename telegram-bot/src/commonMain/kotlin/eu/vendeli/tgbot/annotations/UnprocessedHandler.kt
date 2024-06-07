package eu.vendeli.tgbot.annotations

/**
 * Annotation used to mark the function used to handle updates that not processed.
 * Only one processing point is possible.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UnprocessedHandler
