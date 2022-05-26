package com.github.vendelieu.tgbot.core

import com.github.vendelieu.tgbot.interfaces.ClassManager

/**
 * Default [ClassManager] implementation
 *
 * @constructor Create empty ClassManagerImpl
 */
class ClassManagerImpl : ClassManager {
    override fun getInstance(clazz: Class<*>, vararg initParams: Any?): Any =
        clazz.declaredConstructors.first().newInstance(initParams)
}
