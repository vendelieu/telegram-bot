package eu.vendeli.tgbot.types.chain

import kotlin.reflect.KClass

interface WizardStateManager<T : Any> {
    suspend fun get(key: KClass<out WizardStep>, reference: UserChatReference): T?

    suspend fun set(key: KClass<out WizardStep>, reference: UserChatReference, value: T)

    suspend fun del(key: KClass<out WizardStep>, reference: UserChatReference)
}

data class UserChatReference(
    val userId: Long,
    val chatId: Long,
)
