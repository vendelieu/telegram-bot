package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ClassManager
import kotlin.reflect.KClass

/**
 * Default [ClassManager] implementation
 *
 * @constructor Create empty ClassManagerImpl
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ClassManagerImpl actual constructor() : ClassManager {
    override fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any {
        TODO("Not yet implemented")
    }
}
