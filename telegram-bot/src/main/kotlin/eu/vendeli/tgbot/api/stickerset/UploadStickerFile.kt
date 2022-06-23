package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod

class UploadStickerFileAction(pngSticker: ByteArray) : MediaAction<File> {
    override val method: TgMethod = TgMethod("uploadStickerFile")

    init {
        setDataField("png_sticker")
        setDefaultType(MediaContentType.ImagePng)
        setMedia(pngSticker)
    }

    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun uploadStickerFile(ba: ByteArray) = UploadStickerFileAction(ba)
