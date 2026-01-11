package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.ProcessingCtxKey
import eu.vendeli.tgbot.utils.common.cast
import eu.vendeli.tgbot.utils.common.safeCast

class AdditionalContext internal constructor() : MutableMap<String, Any?> {
    internal val state: MutableMap<ProcessingCtxKey, Any?> = mutableMapOf()

    val regexMatch: MatchResult? get() = state[ProcessingCtxKey.RegexMatch].safeCast()

    override val keys: MutableSet<String>
        get() = state.keys.mapNotNull {
            it.safeCast<ProcessingCtxKey.Custom>()?.name
        }.toMutableSet()

    override val values: MutableCollection<Any?>
        get() = state.entries.filter {
            it.key is ProcessingCtxKey.Custom
        }.map { it.value }.toMutableList()

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = state.entries.filter {
            it.key is ProcessingCtxKey.Custom
        }.map {
            (it.key.cast<ProcessingCtxKey.Custom>().name to it.value).toEntry()
        }.toMutableSet()

    override fun put(key: String, value: Any?): Any? {
        return state.put(ProcessingCtxKey.Custom(key), value)
    }

    override fun remove(key: String): Any? {
        return state.remove(ProcessingCtxKey.Custom(key))
    }

    override fun putAll(from: Map<out String, Any?>) {
        from.forEach { (key, value) -> put(key, value) }
    }

    override fun clear() {
        state.entries.removeAll { it.key is ProcessingCtxKey.Custom }
    }

    override val size: Int
        get() = state.count { it.key is ProcessingCtxKey.Custom }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun containsKey(key: String): Boolean {
        return keys.contains(key)
    }

    override fun containsValue(value: Any?): Boolean {
        return values.contains(value)
    }

    override fun get(key: String): Any? {
        return state[ProcessingCtxKey.Custom(key)]
    }

    internal companion object {
        val EMPTY = AdditionalContext()

        fun <K, V> Pair<K, V>.toEntry() = object : MutableMap.MutableEntry<K, V> {
            override val key: K = first
            override var value: V = second

            override fun setValue(newValue: V): V {
                value = newValue
                return newValue
            }
        }
    }
}
