package eu.vendeli.tgbot.types.internal

import kotlin.reflect.KClass

sealed class StateSelector {
    abstract val state: ProcessedUpdate.() -> Any?
    abstract val type: KClass<*>

    data object Text : StateSelector() {
        override val state: ProcessedUpdate.() -> String = { text }
        override val type = String::class
    }

    data class Custom<T : Any>(
        override val state: ProcessedUpdate.() -> T?,
        override val type: KClass<T>,
    ) : StateSelector()

    companion object {
        inline fun <reified T : Any> Custom(noinline selector: ProcessedUpdate.() -> T?) = Custom(selector, T::class)
    }
}

data class StoredState(
    val update: ProcessedUpdate,
    val selector: StateSelector,
) {
    val text get() = if (selector.type == String::class) selector.state(update) as String else ""
    val data get() = selector.state(update)
}
