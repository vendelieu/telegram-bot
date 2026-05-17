package eu.vendeli.tgbot.interfaces.session

import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.TrackedMessage

/**
 * Backend contract for session message bookkeeping.
 *
 * Implementations decide where tracked entries live (memory, Redis, SQL, …).
 * The default implementation is [eu.vendeli.tgbot.implementations.InMemorySessionStorage].
 */
interface SessionStorage {
    suspend fun add(key: SessionKey, entry: TrackedMessage)

    suspend fun list(key: SessionKey): List<TrackedMessage>

    /** Removes matching entries and returns how many were deleted. */
    suspend fun remove(key: SessionKey, predicate: (TrackedMessage) -> Boolean): Int

    /** Drops every entry for [key]. */
    suspend fun clear(key: SessionKey)
}
