package eu.vendeli.tgbot.types.internal.chain

import eu.vendeli.tgbot.types.User

interface LinkStateManager<T> {
    suspend fun get(user: User): T?

    suspend fun set(user: User, value: T)

    suspend fun del(user: User)
}
