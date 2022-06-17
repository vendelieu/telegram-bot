package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.interfaces.ClassManager

/**
 * Default [ClassManager] implementation
 *
 * @constructor Create empty ClassManagerImpl
 */
class ClassManagerImpl : ClassManager {
    /**
     * Get instance of class
     *
     * @param clazz
     * @param initParams
     * @return class
     */
    override fun getInstance(clazz: Class<*>, vararg initParams: Any?): Any =
        clazz.declaredConstructors.first().newInstance(initParams)
}
