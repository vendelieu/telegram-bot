package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.utils.common.TgException
import kotlin.reflect.KClass

/**
 * Default [ClassManager] implementation
 *
 * @constructor Create empty ClassManagerImpl
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ClassManagerImpl : ClassManager {
    @Suppress("PropertyName", "ktlint:standard:backing-property-naming")
    val _INSTANCES: MutableMap<KClass<*>, Any> = mutableMapOf()
    override fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any = _INSTANCES[kClass]
        ?: throw TgException("Class $kClass not found.")
}

fun TelegramBot.addInstances(block: MutableMap<KClass<*>, Any>.() -> Unit) {
    (config.classManager as? ClassManagerImpl)?._INSTANCES?.apply(block)
}
