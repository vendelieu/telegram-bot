package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.media.InputRichBlock
import eu.vendeli.tgbot.types.media.InputRichBlockListItem
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.media.InputRichMessageMedia
import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.types.msg.toRichText

private const val MIN_HEADING_SIZE = 1
private const val MAX_HEADING_SIZE = 6

/**
 * Builds a list of [InputRichBlock]s.
 *
 * Text of the blocks is described either as a plain [String] or with a nested [RichTextBuilder] block.
 * Blocks that have no dedicated function (media, table, collage, ...) can be added as is with unary plus.
 */
@RichTextDsl
open class RichBlocksBuilder internal constructor() {
    private val blocks = mutableListOf<InputRichBlock>()

    private fun add(block: InputRichBlock) {
        blocks += block
    }

    internal fun build(): List<InputRichBlock> = blocks

    /** Add an already built block, e.g. media or table. */
    operator fun InputRichBlock.unaryPlus() = add(this)

    /** Add a text paragraph. */
    fun paragraph(text: String) = add(InputRichBlock.Paragraph(text.toRichText()))

    /** Add a text paragraph. */
    fun paragraph(block: RichTextBuilder.() -> Unit) = add(InputRichBlock.Paragraph(richText(block)))

    /** Add a section heading, [size] is 1 (largest) to 6 (smallest). */
    fun heading(size: Int, text: String) = add(InputRichBlock.SectionHeading(text.toRichText(), validHeading(size)))

    /** Add a section heading, [size] is 1 (largest) to 6 (smallest). */
    fun heading(size: Int = MIN_HEADING_SIZE, block: RichTextBuilder.() -> Unit) =
        add(InputRichBlock.SectionHeading(richText(block), validHeading(size)))

    /** Add a preformatted text, optionally with its programming [language]. */
    fun preformatted(text: String, language: String? = null) =
        add(InputRichBlock.Preformatted(text.toRichText(), language))

    /** Add a preformatted text, optionally with its programming [language]. */
    fun preformatted(language: String? = null, block: RichTextBuilder.() -> Unit) =
        add(InputRichBlock.Preformatted(richText(block), language))

    /** Add a footer. */
    fun footer(text: String) = add(InputRichBlock.Footer(text.toRichText()))

    /** Add a footer. */
    fun footer(block: RichTextBuilder.() -> Unit) = add(InputRichBlock.Footer(richText(block)))

    /** Add a thinking block. */
    fun thinking(text: String) = add(InputRichBlock.Thinking(text.toRichText()))

    /** Add a thinking block. */
    fun thinking(block: RichTextBuilder.() -> Unit) = add(InputRichBlock.Thinking(richText(block)))

    /** Add a divider. */
    fun divider() = add(InputRichBlock.Divider)

    /** Add a block mathematical expression in LaTeX format. */
    fun mathematicalExpression(expression: String) = add(InputRichBlock.MathematicalExpression(expression))

    /** Add an anchor. */
    fun anchor(name: String) = add(InputRichBlock.Anchor(name))

    /** Add a list of items. */
    fun list(block: RichListBuilder.() -> Unit) = add(InputRichBlock.ListBlock(RichListBuilder().apply(block).build()))

    /** Add a block quotation with nested blocks and optional plain-text [credit]. */
    fun blockquote(credit: String? = null, block: RichBlocksBuilder.() -> Unit) =
        add(InputRichBlock.BlockQuotation(richBlocks(block), credit?.toRichText()))

    /** Add an expandable block quotation with optional plain-text [credit]. */
    fun expandableBlockquote(credit: String? = null, block: RichTextBuilder.() -> Unit) =
        add(InputRichBlock.ExpandableBlockQuotation(richText(block), credit?.toRichText()))

    /** Add a pull quotation with optional plain-text [credit]. */
    fun pullquote(credit: String? = null, block: RichTextBuilder.() -> Unit) =
        add(InputRichBlock.PullQuotation(richText(block), credit?.toRichText()))

    /** Add a collapsible block with an always shown [summary]. */
    fun details(summary: String, isOpen: Boolean? = null, block: RichBlocksBuilder.() -> Unit) =
        add(InputRichBlock.Details(summary.toRichText(), richBlocks(block), isOpen))

    private fun validHeading(size: Int): Int {
        require(size in MIN_HEADING_SIZE..MAX_HEADING_SIZE) {
            "Heading size must be in $MIN_HEADING_SIZE..$MAX_HEADING_SIZE, but was $size"
        }
        return size
    }
}

/**
 * Builds items of a list block.
 */
@RichTextDsl
class RichListBuilder internal constructor() {
    private val items = mutableListOf<InputRichBlockListItem>()

    internal fun build(): List<InputRichBlockListItem> = items.toList()

    /** Add an item consisting of a single plain-text paragraph. */
    fun item(text: String) {
        items += InputRichBlockListItem(listOf(InputRichBlock.Paragraph(text.toRichText())))
    }

    /** Add an item with arbitrary nested blocks. */
    fun item(
        hasCheckbox: Boolean? = null,
        isChecked: Boolean? = null,
        value: Int? = null,
        type: String? = null,
        block: RichBlocksBuilder.() -> Unit,
    ) {
        items += InputRichBlockListItem(richBlocks(block), hasCheckbox, isChecked, value, type)
    }
}

/**
 * Builds an [InputRichMessage]. Exactly one of the blocks, [html] or [markdown] content must be used.
 */
@RichTextDsl
class RichMessageBuilder internal constructor() : RichBlocksBuilder() {
    /** Content of the rich message described using HTML formatting. */
    var html: String? = null

    /** Content of the rich message described using Markdown formatting. */
    var markdown: String? = null

    /** Pass true if the rich message must be shown right-to-left. */
    var isRtl: Boolean? = null

    /** Pass true to skip automatic detection of entities in the text. */
    var skipEntityDetection: Boolean? = null

    private val mediaItems = mutableListOf<InputRichMessageMedia>()

    /** Register media referenced from the [html] or [markdown] content or from blocks. */
    fun media(vararg media: InputRichMessageMedia) {
        mediaItems += media
    }

    internal fun buildMessage(): InputRichMessage {
        val blocks = build().takeIf { it.isNotEmpty() }
        require(listOfNotNull(blocks, html, markdown).size == 1) {
            "Rich message must use exactly one of blocks, html or markdown"
        }
        return InputRichMessage(
            blocks = blocks,
            html = html,
            markdown = markdown,
            media = mediaItems.takeIf { it.isNotEmpty() }?.toList(),
            isRtl = isRtl,
            skipEntityDetection = skipEntityDetection,
        )
    }
}

private fun richBlocks(block: RichBlocksBuilder.() -> Unit): List<InputRichBlock> =
    RichBlocksBuilder().apply(block).build()

/**
 * Build an [InputRichMessage] using [RichMessageBuilder].
 *
 * ```kotlin
 * val message = InputRichMessage {
 *     heading(1, "Release notes")
 *     paragraph {
 *         +"Version "
 *         bold("9.6")
 *         +" is out"
 *     }
 *     list {
 *         item("faster")
 *         item("safer")
 *     }
 * }
 * ```
 */
@Suppress("FunctionName")
fun InputRichMessage(block: RichMessageBuilder.() -> Unit): InputRichMessage =
    RichMessageBuilder().apply(block).buildMessage()
