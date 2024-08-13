package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmName

/**
 * Default [ClassManager] implementation
 *
 * @constructor Create empty ClassManagerImpl
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ClassManagerImpl : ClassManager {
    // keep class instances
    private val instances by lazy { mutableMapOf<String, Any>() }

    /**
     * Get instance of class
     *
     * @param kClass
     * @param initParams
     * @return class
     */
    override fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any = instances.getOrElse(kClass.jvmName) {
        kClass.objectInstance?.also { return it }
        if (initParams.isEmpty()) {
            kClass.primaryConstructor!!.call()
        } else {
            kClass.primaryConstructor!!.call(*initParams)
        }.also { instances[kClass.jvmName] = it }
    }
}
