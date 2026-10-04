package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.common.Location
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.media.Voice
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * This object represents a block in a rich formatted message. Currently, it can be any of the following types:
 * - RichBlockParagraph
 * - RichBlockSectionHeading
 * - RichBlockPreformatted
 * - RichBlockFooter
 * - RichBlockDivider
 * - RichBlockMathematicalExpression
 * - RichBlockAnchor
 * - RichBlockList
 * - RichBlockBlockQuotation
 * - RichBlockExpandableBlockQuotation
 * - RichBlockPullQuotation
 * - RichBlockCollage
 * - RichBlockSlideshow
 * - RichBlockTable
 * - RichBlockDetails
 * - RichBlockMap
 * - RichBlockAnimation
 * - RichBlockAudio
 * - RichBlockDocument
 * - RichBlockPhoto
 * - RichBlockVideo
 * - RichBlockVoiceNote
 * - RichBlockButtons
 * - RichBlockThinking
 *
 * [Api reference](https://core.telegram.org/bots/api#richblock)
 *
 */
@Serializable
sealed class RichBlock {
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
    ) : RichBlock()

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
    ) : RichBlock()

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
    ) : RichBlock()

    /**
     * A footer, corresponding to the HTML tag <footer>.
     * @property text Text of the block
     */
    @Serializable
    @SerialName("footer")
    data class Footer(
        val text: RichText,
    ) : RichBlock()

    /**
     * A divider, corresponding to the HTML tag <hr>.
     */
    @Serializable
    @SerialName("divider")
    data object Divider : RichBlock()

    /**
     * A block mathematical expression.
     * @property expression The mathematical expression in LaTeX format
     */
    @Serializable
    @SerialName("mathematical_expression")
    data class MathematicalExpression(
        val expression: String,
    ) : RichBlock()

    /**
     * An anchor, corresponding to the HTML tag <a name="...">.
     * @property name The name of the anchor
     */
    @Serializable
    @SerialName("anchor")
    data class Anchor(
        val name: String,
    ) : RichBlock()

    /**
     * A list of items, corresponding to the HTML tags <ul> and <ol>.
     * @property items Items of the list
     */
    @Serializable
    @SerialName("list")
    @TgAPI.Name("RichBlockList")
    data class ListBlock(
        val items: List<RichBlockListItem>,
    ) : RichBlock()

    /**
     * A block quotation, corresponding to the HTML tag <blockquote>.
     * @property blocks Content of the block
     * @property credit Optional. Credit of the block
     */
    @Serializable
    @SerialName("blockquote")
    data class BlockQuotation(
        val blocks: List<RichBlock>,
        val credit: RichText? = null,
    ) : RichBlock()

    @Serializable
    @SerialName("expandable_blockquote")
    data class ExpandableBlockQuotation(
        val text: RichText,
        val credit: RichText? = null,
    ) : RichBlock()

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
    ) : RichBlock()

    /**
     * A collage of media.
     * @property blocks Elements of the collage
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("collage")
    data class Collage(
        val blocks: List<RichBlock>,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * A slideshow of media.
     * @property blocks Elements of the slideshow
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("slideshow")
    data class Slideshow(
        val blocks: List<RichBlock>,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * A table, corresponding to the HTML tag <table>.
     * @property cells Cells of the table
     * @property isBordered Optional. True, if the table has borders
     * @property isStriped Optional. True, if the table is striped
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
    ) : RichBlock()

    /**
     * A collapsible block, corresponding to the HTML tag <details>.
     * @property summary Always shown summary of the block
     * @property blocks Content of the block
     * @property isOpen Optional. True, if the content of the block is visible by default
     */
    @Serializable
    @SerialName("details")
    data class Details(
        val summary: RichText,
        val blocks: List<RichBlock>,
        val isOpen: Boolean? = null,
    ) : RichBlock()

    /**
     * A map.
     * @property location Location of the center of the map
     * @property zoom Map zoom level; 13-20
     * @property width Expected width of the map
     * @property height Expected height of the map
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("map")
    @TgAPI.Name("RichBlockMap")
    data class MapBlock(
        val location: Location,
        val zoom: Int,
        val width: Int,
        val height: Int,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * An animation.
     * @property animation The animation
     * @property hasSpoiler Optional. True, if the media preview is covered by a spoiler animation
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("animation")
    data class Animation(
        val animation: eu.vendeli.tgbot.types.media.Animation,
        val hasSpoiler: Boolean? = null,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * An audio file.
     * @property audio The audio
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("audio")
    data class Audio(
        val audio: eu.vendeli.tgbot.types.media.Audio,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    @Serializable
    @SerialName("document")
    data class Document(
        val document: eu.vendeli.tgbot.types.media.Document,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    @Serializable
    @SerialName("buttons")
    data class Buttons(
        val buttons: List<RichMessageButton>,
        val align: String? = null,
    ) : RichBlock()

    /**
     * A photo.
     * @property photo Available sizes of the photo
     * @property hasSpoiler Optional. True, if the media preview is covered by a spoiler animation
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("photo")
    data class Photo(
        val photo: List<PhotoSize>,
        val hasSpoiler: Boolean? = null,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * A video.
     * @property video The video
     * @property hasSpoiler Optional. True, if the media preview is covered by a spoiler animation
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("video")
    data class Video(
        val video: eu.vendeli.tgbot.types.media.Video,
        val hasSpoiler: Boolean? = null,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * A voice note.
     * @property voiceNote The voice note
     * @property caption Optional. Caption of the block
     */
    @Serializable
    @SerialName("voice_note")
    data class VoiceNote(
        val voiceNote: Voice,
        val caption: RichBlockCaption? = null,
    ) : RichBlock()

    /**
     * A thinking block.
     * @property text Text of the block. See https://t.me/addemoji/AIActions for examples of custom emoji that are recommended for usage in the block.
     */
    @Serializable
    @SerialName("thinking")
    data class Thinking(
        val text: RichText,
    ) : RichBlock()
}
