package eu.vendeli.tgbot.annotations

/**
 * Annotation to mark a class as a context provider. Class should be inheritor one of [UserData] or [ClassData].
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class CtxProvider
