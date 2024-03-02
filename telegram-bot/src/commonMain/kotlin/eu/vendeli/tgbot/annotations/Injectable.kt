package eu.vendeli.tgbot.annotations

/**
 * Annotation to mark an object/class as an interface to get the object that can be passed to a function.
 * And target object need to be implementation of [Autowiring] interface.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Injectable
