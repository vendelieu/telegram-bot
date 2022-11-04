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
        if (initParams.isEmpty())
            clazz.declaredConstructors.first().newInstance()
        else
            clazz.declaredConstructors.first().newInstance(initParams)
}
