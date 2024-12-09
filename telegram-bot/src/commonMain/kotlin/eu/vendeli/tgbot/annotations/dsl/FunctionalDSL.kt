package eu.vendeli.tgbot.annotations.dsl

@DslMarker
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPEALIAS,
)
annotation class FunctionalDSL
