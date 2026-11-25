package eu.vendeli.quarkus.extension

import eu.vendeli.tgbot.implementations.ClassManagerImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import io.quarkus.arc.Arc
import jakarta.inject.Singleton
import kotlin.reflect.KClass

/**
 * Resolves handler classes from the CDI container.
 *
 * Classes that are not CDI beans (or are requested with constructor arguments)
 * fall back to KtGram's default [ClassManagerImpl].
 */
@Singleton
class QuarkusClassManager : ClassManager {
    private val fallback = ClassManagerImpl()

    override fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any {
        if (initParams.isEmpty()) {
            val handle = Arc.container().instance(kClass.java)
            if (handle.isAvailable) return handle.get()
        }
        return fallback.getInstance(kClass, *initParams)
    }
}
