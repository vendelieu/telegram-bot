package eu.vendeli.tgbot.interfaces.ctx

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.StoredState

interface ChainStateManager {
    suspend fun getState(user: User, link: String): StoredState?

    suspend fun setState(user: User, link: String, value: StoredState)

    suspend fun clearAllState(user: User, chain: String)
}
