package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.msg.RichText

/**
 * Immutable chain of [RichText] parts produced by the `-` operators:
 *
 * ```kotlin
 * val text = "Hello " - bold("big") - " world"
 * ```
 *
 * Every operator returns a new builder, so a chain can safely be reused as a prefix of several others.
 * Nested [RichText.Chunks] are flattened.
 */
class RichEntitiesBuilder(
    richTexts: List<RichText>,
) {
    val richTexts: List<RichText> = richTexts.flatMap { it.flatten() }

    /** Append plain [string] to the chain. */
    operator fun minus(string: String) = RichEntitiesBuilder(richTexts + RichText.Plain(string))

    /** Append [richText] to the chain. */
    operator fun minus(richText: RichText) = RichEntitiesBuilder(richTexts + richText)

    /** Combine all parts into a single [RichText.Chunks]. */
    fun build() = RichText.Chunks(richTexts)
}

/** Start a chain with plain string followed by [richText]. */
operator fun String.minus(richText: RichText) = RichEntitiesBuilder(listOf(RichText.Plain(this), richText))

/** Start a chain with [this] rich text followed by plain [string]. */
operator fun RichText.minus(string: String) = RichEntitiesBuilder(listOf(this, RichText.Plain(string)))

/** Start a chain with [this] rich text followed by [richText]. */
operator fun RichText.minus(richText: RichText) = RichEntitiesBuilder(listOf(this, richText))

internal fun RichText.flatten(): List<RichText> = if (this is RichText.Chunks) {
    parts.flatMap { it.flatten() }
} else {
    listOf(this)
}
