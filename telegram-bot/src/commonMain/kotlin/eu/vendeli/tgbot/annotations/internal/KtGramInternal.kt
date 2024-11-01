package eu.vendeli.tgbot.annotations.internal

/**
 * API marked with this annotation is internal, and it is not intended to be used outside KtGram.
 * It could be modified or removed without any notice. Using it outside KtGram could cause undefined behaviour and/or
 * any unexpected effects.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal in KtGram and should not be used. It could be removed or changed without notice.",
)
@MustBeDocumented
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
annotation class KtGramInternal
