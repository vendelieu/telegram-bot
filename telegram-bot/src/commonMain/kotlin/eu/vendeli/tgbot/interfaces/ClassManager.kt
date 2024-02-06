package eu.vendeli.tgbot.interfaces

import kotlin.reflect.KClass

/**
 * Interface which is used to process classes, can be inherited to work with DI libraries.
 *
 * @constructor Create empty I class manager
 */
fun interface ClassManager {
    /**
     * Get instance of Class where Command/Input holds.
     *
     * @param kClass
     * @param initParams
     */
    fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any
}
