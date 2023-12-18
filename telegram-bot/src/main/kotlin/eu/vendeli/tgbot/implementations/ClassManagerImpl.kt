package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ClassManager

/**
 * Default [ClassManager] implementation
 *
 * @constructor Create empty ClassManagerImpl
 */
object ClassManagerImpl : ClassManager {
    // keep class instances
    private val instances by lazy { mutableMapOf<String, Any>() }

    /**
     * Get instance of class
     *
     * @param clazz
     * @param initParams
     * @return class
     */
    override fun getInstance(clazz: Class<*>, vararg initParams: Any?): Any = instances.getOrElse(clazz.name) {
        clazz.kotlin.objectInstance?.also { return it }
        if (initParams.isEmpty()) {
            clazz.declaredConstructors.first().newInstance()
        } else {
            clazz.declaredConstructors.first().newInstance(initParams)
        }.also { instances[clazz.name] = it }
    }
}
