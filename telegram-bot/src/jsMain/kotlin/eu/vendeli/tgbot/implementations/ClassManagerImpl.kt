package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.utils.TgException
import kotlin.reflect.KClass
import kotlin.reflect.createInstance

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
    @OptIn(ExperimentalJsReflectionCreateInstance::class)
    override fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any = instances.getOrElse(kClass.js.name) {
        if (initParams.isEmpty()) {
            kClass.createInstance()
        } else {
            throw TgException("Passing class init params not supported.")
        }.also { instances[kClass.js.name] = it }
    }
}
