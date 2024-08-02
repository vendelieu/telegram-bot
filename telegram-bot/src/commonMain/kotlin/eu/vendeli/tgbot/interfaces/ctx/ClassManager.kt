package eu.vendeli.tgbot.interfaces.ctx

import kotlin.reflect.KClass

/**
 * Interface, which is used to process classes, can be inherited to work with DI libraries.
 *
 * @constructor Create empty I class manager
 */
interface ClassManager {
    /**
     * Get instance of Class where Command/Input holds.
     *
     * @param kClass
     * @param initParams
     */
    fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any = TODO()
}
