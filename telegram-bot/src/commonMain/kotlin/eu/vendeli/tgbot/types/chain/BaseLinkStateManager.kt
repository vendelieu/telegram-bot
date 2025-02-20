package eu.vendeli.tgbot.types.chain

import co.touchlab.stately.collections.ConcurrentMutableMap
import eu.vendeli.tgbot.types.component.IdLong
import eu.vendeli.tgbot.types.component.userOrNull

open class BaseLinkStateManager<V>(
    stateSelector: KeySelector<IdLong> = KeySelector { it.userOrNull },
) : LinkStateManager<IdLong, V> {
    protected val data: ConcurrentMutableMap<Long, V> = ConcurrentMutableMap()
    override val stateKey: KeySelector<IdLong> = stateSelector

    override suspend fun get(key: IdLong): V? = data[key.id]

    override suspend fun set(key: IdLong, value: V) {
        data[key.id] = value
    }

    override suspend fun del(key: IdLong) {
        data.remove(key.id)
    }
}
