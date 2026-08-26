package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.common.Location
import eu.vendeli.tgbot.types.msg.RichBlockCaption
import eu.vendeli.tgbot.types.msg.RichBlockTableCell
import eu.vendeli.tgbot.types.msg.RichMessageButton
import eu.vendeli.tgbot.types.msg.RichText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * This object represents a block in a rich formatted message to be sent. Currently, it can be any of the following types:
 * - InputRichBlockParagraph
 * - InputRichBlockSectionHeading
 * - InputRichBlockPreformatted
 * - InputRichBlockFooter
 * - InputRichBlockDivider
 * - InputRichBlockMathematicalExpression
 * - InputRichBlockAnchor
 * - InputRichBlockList
 * - InputRichBlockBlockQuotation
 * - InputRichBlockExpandableBlockQuotation
 * - InputRichBlockPullQuotation
 * - InputRichBlockCollage
 * - InputRichBlockSlideshow
 * - InputRichBlockTable
 * - InputRichBlockDetails
 * - InputRichBlockMap
 * - InputRichBlockAnimation
 * - InputRichBlockAudio
 * - InputRichBlockDocument
 * - InputRichBlockPhoto
 * - InputRichBlockVideo
 * - InputRichBlockVoiceNote
 * - InputRichBlockButtons
 * - InputRichBlockThinking
 *
 * [Api reference](https://core.telegram.org/bots/api#inputrichblock)
 *
 */
@Serializable
sealed class InputRichBlock {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    /**
     * A text paragraph, corresponding to the HTML tag <p>.
     * @property text Text of the block
     */
    @Serializable
    @SerialName("paragraph")
    data class Paragraph(
        val text: RichText,
    ) : InputRichBlock()

    /**
     * A section heading, corresponding to the HTML tags <h1> to <h6>.
     * @property text Text of the block
     * @property size Relative size of the text font; 1-6, 1 is the largest, 6 is the smallest
     */
    @Serializable
    @SerialName("heading")
    data class SectionHeading(
        val text: RichText,
        val size: Int,
    ) : InputRichBlock()

    /**
     * A preformatted text, corresponding to the HTML tag <pre>.
     * @property text Text of the block
     * @property language Optional. The programming language of the text
     */
    @Serializable
    @SerialName("pre")
    data class Preformatted(
        val text: RichText,
        val language: String? = null,
    ) : InputRichBlock()

    /**
     * A footer, corresponding to the HTML tag <footer>.
     * @property text Text of the block
     */
    @Serializable
    @SerialName("footer")
    data class Footer(
        val text: RichText,
    ) : InputRichBlock()

    /**
     * A divider, corresponding to the HTML tag <hr>.
     */
    @Serializable
    @SerialName("divider")
    data object Divider : InputRichBlock()

    /**
     * A block mathematical expression.
     * @property expression The mathematical expression in LaTeX format
     */
    @Serializable
    @SerialName("mathematical_expression")
    data class MathematicalExpression(
        val expression: String,
    ) : InputRichBlock()

    /**
     * An anchor, corresponding to the HTML tag <a name="...">.
     * @property name The name of the anchor
     */
    @Serializable
    @SerialName("anchor")
    data class Anchor(
        val name: String,
    ) : InputRichBlock()

    /**
     * A list of items, corresponding to the HTML tags <ul> and <ol>.
     * @property items Items of the list
     */
    @Serializable
    @SerialName("list")
    @TgAPI.Name("InputRichBlockList")
    data class ListBlock(
        val items: List<InputRichBlockListItem>,
    ) : InputRichBlock()

    /**
     * A block quotation, corresponding to the HTML tag <blockquote>.
     * @property blocks Content of the block
     * @property credit Optional. Credit of the block
     */
    @Serializable
    @SerialName("blockquote")
    data class BlockQuotation(
        val blocks: List<InputRichBlock>,
        val credit: RichText? = null,
    ) : InputRichBlock()

    @Serializable
    @SerialName("expandable_blockquote")
    data class ExpandableBlockQuotation(
        val text: RichText,
        val credit: RichText? = null,
    ) : InputRichBlock()

    /**
     * A pull quotation, corresponding to the HTML tag <aside>.
     * @property text Text of the block
     * @property credit Optional. Credit of the block
     */
    @Serializable
    @SerialName("pullquote")
    data class PullQuotation(
        val text: RichText,
        val credit: RichText? = null,
    ) : InputRichBlock()

    /**
     * A collage of media.
     * @property blocks Elements of the collage
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("collage")
    data class Collage(
        val blocks: List<InputRichBlock>,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * A slideshow of media.
     * @property blocks Elements of the slideshow
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("slideshow")
    data class Slideshow(
        val blocks: List<InputRichBlock>,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * A table, corresponding to the HTML tag <table>.
     * @property cells Cells of the table
     * @property isBordered Optional. Pass True if the table has borders
     * @property isStriped Optional. Pass True if the table is striped
     * @property caption Optional. Caption of the table
     */
    @Serializable
    @SerialName("table")
    data class Table(
        val cells: List<List<RichBlockTableCell>>,
        val isBordered: Boolean? = null,
        val isStriped: Boolean? = null,
        val isCompact: Boolean? = null,
        val caption: RichText? = null,
    ) : InputRichBlock()

    /**
     * A collapsible block, corresponding to the HTML tag <details>.
     * @property summary Always shown summary of the block
     * @property blocks Content of the block
     * @property isOpen Optional. Pass True if the content of the block is visible by default
     */
    @Serializable
    @SerialName("details")
    data class Details(
        val summary: RichText,
        val blocks: List<InputRichBlock>,
        val isOpen: Boolean? = null,
    ) : InputRichBlock()

    /**
     * A map.
     * @property location Location of the center of the map
     * @property zoom Map zoom level; 0-24
     * @property width Map width; 0-10000
     * @property height Map height; 0-10000
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("map")
    @TgAPI.Name("InputRichBlockMap")
    data class MapBlock(
        val location: Location,
        val zoom: Int? = null,
        val width: Int? = null,
        val height: Int? = null,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * An animation.
     * @property animation The animation. Caption is ignored.
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("animation")
    data class Animation(
        val animation: InputMedia.Animation,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * An audio file.
     * @property audio The audio. Caption is ignored.
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("audio")
    data class Audio(
        val audio: InputMedia.Audio,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    @Serializable
    @SerialName("document")
    data class Document(
        val document: InputMedia.Document,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    @Serializable
    @SerialName("buttons")
    data class Buttons(
        val buttons: List<RichMessageButton>,
        val align: String? = null,
    ) : InputRichBlock()

    /**
     * A photo.
     * @property photo The photo. Caption is ignored.
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("photo")
    data class Photo(
        val photo: InputMedia.Photo,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * A video.
     * @property video The video. Caption is ignored.
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("video")
    data class Video(
        val video: InputMedia.Video,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * A voice note.
     * @property voiceNote The voice note. Caption is ignored.
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("voice_note")
    data class VoiceNote(
        val voiceNote: InputMediaVoiceNote,
        val caption: RichBlockCaption? = null,
    ) : InputRichBlock()

    /**
     * A thinking block.
     * @property text Text of the block. See https://t.me/addemoji/AIActions for examples of custom emoji that are recommended for usage in the block.
     */
    @Serializable
    @SerialName("thinking")
    data class Thinking(
        val text: RichText,
    ) : InputRichBlock()
}
