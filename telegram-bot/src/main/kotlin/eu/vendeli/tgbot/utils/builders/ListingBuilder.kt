package eu.vendeli.tgbot.utils.builders

/**
 * Builder for declaring enumeration elements.
 *
 * @param T
 */
class ListingBuilder<T : Any> {
    private val list = mutableListOf<T>()

    /**
     * Function to add element to result list.
     *
     * @param element
     */
    fun add(element: T) {
        list += element
    }

    /**
     * Shorcut method for backward compatability.
     *
     * @param elements
     */
    fun arrayOf(vararg elements: T) = list.addAll(elements)

    /**
     * Shorcut method for backward compatability.
     *
     * @param elements
     */
    fun listOf(vararg elements: T) = list.addAll(elements)

    /**
     * Operator function to add elements by [unaryPlus] operator.
     *
     */
    operator fun T.unaryPlus() = add(this)

    internal companion object {
        fun <T : Any> build(block: ListingBuilder<T>.() -> Unit): List<T> = ListingBuilder<T>().apply(block).list
    }
}
