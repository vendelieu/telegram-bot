package eu.vendeli.tgbot.annotations

/**
 * Annotation used to mark the function used to handle updates that not processed.
 * Only one processing point is possible.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UnprocessedHandler
