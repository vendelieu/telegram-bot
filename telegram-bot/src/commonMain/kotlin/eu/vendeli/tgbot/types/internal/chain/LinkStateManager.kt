package eu.vendeli.tgbot.types.internal.chain

interface LinkStateManager<K, V> {
    val stateKey: KeySelector<K>

    suspend fun get(key: K): V?

    suspend fun set(key: K, value: V)

    suspend fun del(key: K)
}
