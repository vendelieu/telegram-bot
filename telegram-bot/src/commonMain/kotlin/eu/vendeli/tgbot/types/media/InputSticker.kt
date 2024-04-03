package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.internal.ImplicitFile
import kotlinx.serialization.Serializable

/**
 * This object describes a sticker to be added to a sticker set.
 *
 * Api reference: https://core.telegram.org/bots/api#inputsticker
 * @property sticker The added sticker. Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, upload a new one using multipart/form-data, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. Animated and video stickers can't be uploaded via HTTP URL. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @property format Format of the added sticker, must be one of "static" for a .WEBP or .PNG image, "animated" for a .TGS animation, "video" for a WEBM video
 * @property emojiList List of 1-20 emoji associated with the sticker
 * @property maskPosition Optional. Position where the mask should be placed on faces. For "mask" stickers only.
 * @property keywords Optional. List of 0-20 search keywords for the sticker with total length of up to 64 characters. For "regular" and "custom_emoji" stickers only.
 */
@Serializable
data class InputSticker(
    var sticker: ImplicitFile,
    val format: StickerFormat,
    val emojiList: List<String>,
    val maskPosition: MaskPosition? = null,
    val keywords: List<String>? = null,
)
