package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.common.toImplicitFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Represents a voice note to be sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmediavoicenote)
 * @property media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @property caption Optional. Caption of the voice message to be sent, 0-1024 characters after entities parsing
 * @property parseMode Optional. Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @property captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @property duration Optional. Duration of the voice message in seconds
 */
@Serializable
@SerialName("voice_note")
@TgAPI.Name("InputMediaVoiceNote")
data class InputMediaVoiceNote(
    override var media: ImplicitFile,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Int? = null,
) : InputRichMedia {
    constructor(
        media: String,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Int? = null,
    ) : this(media.toImplicitFile(), caption, parseMode, captionEntities, duration)

    constructor(
        media: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Int? = null,
    ) : this(media.toImplicitFile(), caption, parseMode, captionEntities, duration)

    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }
}
