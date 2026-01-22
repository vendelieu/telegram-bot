package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.types.chain.UserChatReference
import eu.vendeli.tgbot.types.chain.WizardStateManager
import eu.vendeli.tgbot.types.chain.WizardStep
import kotlin.reflect.KClass

/**
 * Abstract implementation of the [WizardStateManager] interface that manages state using an in-memory map.
 *
 * This class provides basic methods for persisting, retrieving, and deleting state associated with
 * a specific pair of [WizardStep] and [UserChatReference]. It uses a mutable map (`storage`)
 * where keys are a combination of the [WizardStep] class and [UserChatReference], serialized as strings.
 *
 * @param T The type of the state object being managed.
 */
abstract class MapStateManager<T : Any> : WizardStateManager<T> {
    private val storage = mutableMapOf<String, T>()

    override suspend fun get(
        key: KClass<out WizardStep>,
        reference: UserChatReference,
    ): T? = storage["$key-$reference"]

    override suspend fun set(
        key: KClass<out WizardStep>,
        reference: UserChatReference,
        value: T,
    ) {
        storage["$key-$reference"] = value
    }

    override suspend fun del(
        key: KClass<out WizardStep>,
        reference: UserChatReference,
    ) {
        storage -= "$key-$reference"
    }
}

class MapStringStateManager : MapStateManager<String>()
class MapIntStateManager : MapStateManager<Int>()
class MapLongStateManager : MapStateManager<Long>()
