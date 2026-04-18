package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.session.SessionStorage
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.TrackedMessage
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * In-memory [SessionStorage] backed by Ktor's [ConcurrentMap].
 *
 * Per-key mutation is coordinated by a suspending [Mutex] so `add`/`remove`/`list` are
 * observed atomically for a given [SessionKey] (coroutines is already a core dependency,
 * so this stays KMP-safe without pulling in atomicfu).
 */
class InMemorySessionStorage : SessionStorage {
    private val storage = ConcurrentMap<SessionKey, MutableList<TrackedMessage>>()
    private val locks = ConcurrentMap<SessionKey, Mutex>()

    private fun lockFor(key: SessionKey): Mutex = locks.computeIfAbsent(key) { Mutex() }

    override suspend fun add(key: SessionKey, entry: TrackedMessage) = lockFor(key).withLock {
        storage.computeIfAbsent(key) { mutableListOf() }.add(entry)
        Unit
    }

    override suspend fun list(key: SessionKey): List<TrackedMessage> = lockFor(key).withLock {
        storage[key]?.toList() ?: emptyList()
    }

    override suspend fun remove(key: SessionKey, predicate: (TrackedMessage) -> Boolean): Int =
        lockFor(key).withLock {
            val bucket = storage[key] ?: return@withLock 0
            val before = bucket.size
            bucket.removeAll(predicate)
            before - bucket.size
        }

    override suspend fun clear(key: SessionKey) = lockFor(key).withLock {
        storage.remove(key)
        Unit
    }
}
