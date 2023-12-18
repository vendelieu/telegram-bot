package eu.vendeli.tgbot.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoWiring<T : Any>(val type: KClass<T>)
