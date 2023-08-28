package eu.vendeli.tgbot.interfaces

/**
 * Interface which is used to process classes, can be inherited to work with DI libraries.
 *
 * @constructor Create empty I class manager
 */
fun interface ClassManager {
    /**
     * Get instance of Class where Command/Input holds.
     *
     * @param clazz
     * @param initParams
     */
    fun getInstance(clazz: Class<*>, vararg initParams: Any?): Any
}
