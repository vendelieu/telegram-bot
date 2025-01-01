@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SetStickerSetThumbnailAction(
    name: String,
    userId: Long,
    format: StickerFormat,
    thumbnail: ImplicitFile? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setStickerSetThumbnail")
    override val method = "setStickerSetThumbnail"
    override val returnType = getReturnType()

    init {
        if (thumbnail != null) handleImplicitFile(thumbnail, "thumbnail")
        parameters["name"] = name.toJsonElement()
        parameters["user_id"] = userId.toJsonElement()
        parameters["format"] = format.encodeWith(StickerFormat.serializer())
    }
}

/**
 * Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match the format of the stickers in the set. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setstickersetthumbnail)
 * @param name Sticker set name
 * @param userId User identifier of the sticker set owner
 * @param thumbnail A .WEBP or .PNG image with the thumbnail, must be up to 128 kilobytes in size and have a width and height of exactly 100px, or a .TGS animation with a thumbnail up to 32 kilobytes in size (see https://core.telegram.org/stickers#animation-requirements for animated sticker technical requirements), or a .WEBM video with the thumbnail up to 32 kilobytes in size; see https://core.telegram.org/stickers#video-requirements for video sticker technical requirements. Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Animated and video sticker set thumbnails can't be uploaded via HTTP URL. If omitted, then the thumbnail is dropped and the first sticker is used as the thumbnail.
 * @param format Format of the thumbnail, must be one of "static" for a .WEBP or .PNG image, "animated" for a .TGS animation, or "video" for a .WEBM video
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setStickerSetThumbnail(name: String, userId: Long, format: StickerFormat, thumbnail: ImplicitFile? = null) =
    SetStickerSetThumbnailAction(name, userId, format, thumbnail)
