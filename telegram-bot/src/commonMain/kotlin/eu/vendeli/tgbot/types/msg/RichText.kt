package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.common.TgException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer

/**
 * This object represents a rich formatted text. Currently, it can be either a String for plain text
 * (represented as [RichText.Plain]), an Array of RichText (represented as [RichText.Chunks]),
 * or any of the following types:
 * - RichTextBold
 * - RichTextItalic
 * - RichTextUnderline
 * - RichTextStrikethrough
 * - RichTextSpoiler
 * - RichTextDateTime
 * - RichTextTextMention
 * - RichTextSubscript
 * - RichTextSuperscript
 * - RichTextMarked
 * - RichTextCode
 * - RichTextCustomEmoji
 * - RichTextMathematicalExpression
 * - RichTextUrl
 * - RichTextEmailAddress
 * - RichTextPhoneNumber
 * - RichTextBankCardNumber
 * - RichTextMention
 * - RichTextHashtag
 * - RichTextCashtag
 * - RichTextBotCommand
 * - RichTextAnchor
 * - RichTextAnchorLink
 * - RichTextReference
 * - RichTextReferenceLink
 *
 * [Api reference](https://core.telegram.org/bots/api#richtext)
 *
 */
@Serializable(RichText.Companion::class)
@Suppress("TooManyFunctions")
sealed class RichText {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    open val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    /**
     * Plain text without formatting, represented in the API as a bare String.
     */
    @TgAPI.Ignore
    data class Plain(
        val text: String,
    ) : RichText() {
        override val type: String = "plain"
    }

    /**
     * Concatenation of several rich texts, represented in the API as an Array of RichText.
     */
    @TgAPI.Ignore
    data class Chunks(
        val parts: List<RichText>,
    ) : RichText() {
        constructor(vararg parts: RichText) : this(parts.toList())

        override val type: String = "chunks"
    }

    /**
     * A bold text.
     * @property text The text
     */
    @Serializable
    @SerialName("bold")
    data class Bold(
        val text: RichText,
    ) : RichText()

    /**
     * An italic text.
     * @property text The text
     */
    @Serializable
    @SerialName("italic")
    data class Italic(
        val text: RichText,
    ) : RichText()

    /**
     * An underlined text.
     * @property text The text
     */
    @Serializable
    @SerialName("underline")
    data class Underline(
        val text: RichText,
    ) : RichText()

    /**
     * A strikethrough text.
     * @property text The text
     */
    @Serializable
    @SerialName("strikethrough")
    data class Strikethrough(
        val text: RichText,
    ) : RichText()

    /**
     * A spoiler text.
     * @property text The text
     */
    @Serializable
    @SerialName("spoiler")
    data class Spoiler(
        val text: RichText,
    ) : RichText()

    /**
     * Formatted date and time.
     * @property text The text
     * @property unixTime The Unix time associated with the entity
     * @property dateTimeFormat The string that defines the formatting of the date and time.
     */
    @Serializable
    @SerialName("date_time")
    data class DateTime(
        val text: RichText,
        val unixTime: Long,
        val dateTimeFormat: String,
    ) : RichText()

    /**
     * A text mention of a user without a username.
     * @property text The text
     * @property user The mentioned user
     */
    @Serializable
    @SerialName("text_mention")
    data class TextMention(
        val text: RichText,
        val user: User,
    ) : RichText()

    /**
     * A subscript text.
     * @property text The text
     */
    @Serializable
    @SerialName("subscript")
    data class Subscript(
        val text: RichText,
    ) : RichText()

    /**
     * A superscript text.
     * @property text The text
     */
    @Serializable
    @SerialName("superscript")
    data class Superscript(
        val text: RichText,
    ) : RichText()

    /**
     * A marked (highlighted) text.
     * @property text The text
     */
    @Serializable
    @SerialName("marked")
    data class Marked(
        val text: RichText,
    ) : RichText()

    /**
     * A text formatted as code.
     * @property text The text
     */
    @Serializable
    @SerialName("code")
    data class Code(
        val text: RichText,
    ) : RichText()

    /**
     * A custom emoji.
     * @property customEmojiId Unique identifier of the custom emoji. Use getCustomEmojiStickers to get full information about the sticker.
     * @property alternativeText Alternative emoji for the custom emoji
     */
    @Serializable
    @SerialName("custom_emoji")
    data class CustomEmoji(
        val customEmojiId: String,
        val alternativeText: String,
    ) : RichText()

    /**
     * An inline mathematical expression.
     * @property expression The expression in LaTeX format
     */
    @Serializable
    @SerialName("mathematical_expression")
    data class MathematicalExpression(
        val expression: String,
    ) : RichText()

    /**
     * A link.
     * @property text The text
     * @property url URL of the link
     */
    @Serializable
    @SerialName("url")
    data class Url(
        val text: RichText,
        val url: String,
    ) : RichText()

    /**
     * An email address.
     * @property text The text
     * @property emailAddress The email address
     */
    @Serializable
    @SerialName("email_address")
    data class EmailAddress(
        val text: RichText,
        val emailAddress: String,
    ) : RichText()

    /**
     * A phone number.
     * @property text The text
     * @property phoneNumber The phone number
     */
    @Serializable
    @SerialName("phone_number")
    data class PhoneNumber(
        val text: RichText,
        val phoneNumber: String,
    ) : RichText()

    /**
     * A bank card number.
     * @property text The text
     * @property bankCardNumber The bank card number
     */
    @Serializable
    @SerialName("bank_card_number")
    data class BankCardNumber(
        val text: RichText,
        val bankCardNumber: String,
    ) : RichText()

    /**
     * A mention of a user by username.
     * @property text The text
     * @property username The username
     */
    @Serializable
    @SerialName("mention")
    data class Mention(
        val text: RichText,
        val username: String,
    ) : RichText()

    /**
     * A hashtag.
     * @property text The text
     * @property hashtag The hashtag
     */
    @Serializable
    @SerialName("hashtag")
    data class Hashtag(
        val text: RichText,
        val hashtag: String,
    ) : RichText()

    /**
     * A cashtag.
     * @property text The text
     * @property cashtag The cashtag
     */
    @Serializable
    @SerialName("cashtag")
    data class Cashtag(
        val text: RichText,
        val cashtag: String,
    ) : RichText()

    /**
     * A bot command.
     * @property text The text
     * @property botCommand The bot command
     */
    @Serializable
    @SerialName("bot_command")
    data class BotCommand(
        val text: RichText,
        val botCommand: String,
    ) : RichText()

    /**
     * An anchor.
     * @property name The name of the anchor
     */
    @Serializable
    @SerialName("anchor")
    data class Anchor(
        val name: String,
    ) : RichText()

    /**
     * A link to an anchor in the same message.
     * @property text The link text
     * @property anchorName The name of the anchor. If the name is empty, then the link brings back to the top of the message.
     */
    @Serializable
    @SerialName("anchor_link")
    data class AnchorLink(
        val text: RichText,
        val anchorName: String,
    ) : RichText()

    /**
     * A reference.
     * @property text Text of the reference
     * @property name The name of the reference
     */
    @Serializable
    @SerialName("reference")
    data class Reference(
        val text: RichText,
        val name: String,
    ) : RichText()

    /**
     * A link to a reference in the same message.
     * @property text The link text
     * @property referenceName The name of the reference
     */
    @Serializable
    @SerialName("reference_link")
    data class ReferenceLink(
        val text: RichText,
        val referenceName: String,
    ) : RichText()

    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    internal companion object : KSerializer<RichText> {
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("eu.vendeli.tgbot.types.msg.RichText", PolymorphicKind.SEALED)

        private val subtypeSerializers: Map<String, KSerializer<out RichText>> by lazy {
            listOf(
                Bold.serializer(),
                Italic.serializer(),
                Underline.serializer(),
                Strikethrough.serializer(),
                Spoiler.serializer(),
                DateTime.serializer(),
                TextMention.serializer(),
                Subscript.serializer(),
                Superscript.serializer(),
                Marked.serializer(),
                Code.serializer(),
                CustomEmoji.serializer(),
                MathematicalExpression.serializer(),
                Url.serializer(),
                EmailAddress.serializer(),
                PhoneNumber.serializer(),
                BankCardNumber.serializer(),
                Mention.serializer(),
                Hashtag.serializer(),
                Cashtag.serializer(),
                BotCommand.serializer(),
                Anchor.serializer(),
                AnchorLink.serializer(),
                Reference.serializer(),
                ReferenceLink.serializer(),
            ).associateBy { it.descriptor.serialName }
        }

        override fun deserialize(decoder: Decoder): RichText {
            val input = decoder as? JsonDecoder ?: throw TgException("RichText supports only JSON format")
            return when (val element = input.decodeJsonElement()) {
                is JsonPrimitive -> {
                    Plain(element.content)
                }

                is JsonArray -> {
                    Chunks(element.map { input.json.decodeFromJsonElement(this, it) })
                }

                is JsonObject -> {
                    val typeName = element["type"]?.jsonPrimitive?.content
                    val serializer = subtypeSerializers[typeName]
                        ?: throw TgException("Unsupported rich text type - $typeName")
                    input.json.decodeFromJsonElement(serializer, element)
                }
            }
        }

        override fun serialize(encoder: Encoder, value: RichText) {
            val output = encoder as? JsonEncoder ?: throw TgException("RichText supports only JSON format")
            output.encodeJsonElement(value.toJsonElement(output.json))
        }

        private fun RichText.toJsonElement(json: Json): JsonElement = when (this) {
            is Plain -> {
                JsonPrimitive(text)
            }

            is Chunks -> {
                JsonArray(parts.map { it.toJsonElement(json) })
            }

            else -> {
                @Suppress("UNCHECKED_CAST")
                val serializer = this::class.serializer() as KSerializer<RichText>
                JsonObject(
                    buildMap {
                        put("type", JsonPrimitive(type))
                        putAll(json.encodeToJsonElement(serializer, this@toJsonElement).jsonObject)
                    },
                )
            }
        }
    }
}

/**
 * Wrap this string into a plain [RichText].
 */
fun String.toRichText(): RichText = RichText.Plain(this)

/**
 * Combine the given rich texts into a single [RichText].
 */
fun List<RichText>.toRichText(): RichText = RichText.Chunks(this)
