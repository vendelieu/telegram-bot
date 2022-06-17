package eu.vendeli.tgbot.interfaces

/**
 * Interface which is used to process classes, can be inherited to work with DI libraries.
 *
 * @constructor Create empty I class manager
 */
interface ClassManager {
    fun getInstance(clazz: Class<*>, vararg initParams: Any?): Any
    // if you want to use DI
}
