package eu.vendeli.tgbot.types.internal.chain

import co.touchlab.stately.collections.ConcurrentMutableMap
import eu.vendeli.tgbot.types.User

open class BaseLinkStateManager<T> : LinkStateManager<T> {
    protected val data: ConcurrentMutableMap<Long, T> = ConcurrentMutableMap()

    override suspend fun get(user: User): T? = data[user.id]

    override suspend fun set(user: User, value: T) {
        data[user.id] = value
    }

    override suspend fun del(user: User) {
        data.remove(user.id)
    }
}
