package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.ImplicitFile.Str
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.serde.GenericValueSerializer
import kotlinx.serialization.Serializable

/**
 * Sticker set file options
 *
 *  PNG image with the thumbnail, must be up to 128 kilobytes in size and have width and height exactly 100px,
 *  or a TGS animation with the thumbnail up to 32 kilobytes in size;
 *  see [animated sticker requirements](https://core.telegram.org/stickers#animated-sticker-requirements)
 *  for animated sticker technical requirements,
 *  or a WEBM video with the thumbnail up to 32 kilobytes in size;
 *  see [video sticker technical requirements](https://core.telegram.org/stickers#video-sticker-requirements).
 */
@Serializable
sealed class StickerFile(
    val data: ImplicitFile,
    val stickerFormat: StickerFormat,
) {
    @Serializable
    class PNG(val file: ImplicitFile) : StickerFile(file, StickerFormat.Static)

    @Serializable
    class TGS(val file: ImplicitFile) : StickerFile(file, StickerFormat.Animated)

    @Serializable
    class WEBM(val file: ImplicitFile) : StickerFile(file, StickerFormat.Video)

    @Serializable
    class WEBP(val file: ImplicitFile) : StickerFile(file, StickerFormat.Static)

    @Serializable(FileId.Companion::class)
    data class FileId(
        val fileId: String,
    ) : StickerFile(Str(""), StickerFormat.Static) {
        internal companion object : GenericValueSerializer<FileId>({ fileId })
    }

    @Serializable
    internal class AttachedFile(
        val file: Str,
        val format: StickerFormat,
    ) : StickerFile(file, format)
}
