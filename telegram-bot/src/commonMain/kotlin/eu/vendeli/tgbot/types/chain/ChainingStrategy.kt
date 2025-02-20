package eu.vendeli.tgbot.types.chain

/**
 * Define `@InputChain` link chaining strategy.
 */
sealed class ChainingStrategy {
    /**
     * Chain by declaration.
     */
    data object Default : ChainingStrategy()

    /**
     * Chain to a specified link.
     */
    data class LinkTo<T : Link<*>>(
        val target: () -> T,
    ) : ChainingStrategy()

    /**
     * Don't chain, maybe convenient for more custom logic.
     */
    data object DoNothing : ChainingStrategy()
}
