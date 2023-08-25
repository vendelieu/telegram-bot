package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.ImplicitFile.Str
import eu.vendeli.tgbot.types.media.StickerFormat

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
sealed class StickerFile(
    val data: ImplicitFile<*>,
    val stickerFormat: StickerFormat,
    internal val contentType: MediaContentType,
) {
    class PNG(file: ImplicitFile<*>) : StickerFile(file, StickerFormat.Static, MediaContentType.ImagePng)

    class TGS(file: ImplicitFile<*>) : StickerFile(file, StickerFormat.Animated, MediaContentType.ImageTgs)

    class WEBM(file: ImplicitFile<*>) : StickerFile(file, StickerFormat.Video, MediaContentType.VideoWebm)

    class WEBP(file: ImplicitFile<*>) : StickerFile(file, StickerFormat.Static, MediaContentType.ImageWebp)

    internal class AttachedFile(
        file: Str,
        format: StickerFormat,
        contentType: MediaContentType,
    ) : StickerFile(file, format, contentType)
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun StickerFile.toAttached(name: String) =
    StickerFile.AttachedFile(Str("attach://$name"), stickerFormat, contentType)
