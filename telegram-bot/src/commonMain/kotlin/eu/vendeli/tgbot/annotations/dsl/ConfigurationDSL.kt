package eu.vendeli.tgbot.annotations.dsl

@DslMarker
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPEALIAS,
)
annotation class ConfigurationDSL
