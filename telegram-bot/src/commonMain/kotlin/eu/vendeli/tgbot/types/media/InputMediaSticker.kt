package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.utils.common.toImplicitFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Represents a sticker file to be sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmediasticker)
 * @property media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @property emoji Optional. Emoji associated with the sticker; only for just uploaded stickers
 */
@Serializable
@SerialName("sticker")
data class InputMediaSticker(
    val media: ImplicitFile,
    val emoji: String? = null,
) : InputPollOptionMedia {
    constructor(media: String, emoji: String? = null) : this(media.toImplicitFile(), emoji)
    constructor(media: InputFile, emoji: String? = null) : this(media.toImplicitFile(), emoji)

    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }
}
