package eu.vendeli.tgbot.implementations

import co.touchlab.stately.collections.ConcurrentMutableMap
import eu.vendeli.tgbot.interfaces.ctx.ChainStateManager
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.StoredState

abstract class ChainStateManagerImpl : ChainStateManager {
    val state = ConcurrentMutableMap<User, ConcurrentMutableMap<String, StoredState>>()

    override suspend fun getState(user: User, link: String): StoredState? = state[user]?.get(link)

    override suspend fun setState(user: User, link: String, value: StoredState): Unit = state.block {
        state[user]?.put(link, value)
    }

    override suspend fun clearAllState(user: User, chain: String): Unit = state.block {
        state[user]?.entries?.removeAll { it.key.startsWith(chain) }
    }
}
