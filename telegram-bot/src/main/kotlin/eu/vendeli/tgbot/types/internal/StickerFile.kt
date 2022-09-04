package eu.vendeli.tgbot.types.internal

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
sealed class StickerFile(val file: ByteArray) {
    abstract val contentType: MediaContentType

    class PNG(file: ByteArray) : StickerFile(file) {
        override val contentType = MediaContentType.ImagePng
    }

    class TGS(file: ByteArray) : StickerFile(file) {
        override val contentType = MediaContentType.ImageTgs
    }

    class WEBM(file: ByteArray) : StickerFile(file) {
        override val contentType = MediaContentType.VideoWebm
    }

    /**
     * Converts Sticker to [ImplicitFile.InputFile](ImplicitFile.InputFile)
     *
     */
    fun toImplicitFile() = ImplicitFile.InputFile(this.file)
}
