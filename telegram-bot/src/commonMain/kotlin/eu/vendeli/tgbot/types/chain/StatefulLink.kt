package eu.vendeli.tgbot.types.chain

abstract class StatefulLink<K, V> : Link<V> {
    abstract val state: LinkStateManager<K, V>
    override val retryAfterBreak = true
    override val breakCondition: BreakCondition? = null
    override val beforeAction: ChainAction? = null
    override val afterAction: ChainAction? = null
}
