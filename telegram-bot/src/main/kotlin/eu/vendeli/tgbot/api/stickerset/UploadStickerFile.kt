package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod

class UploadStickerFileAction(private val pngSticker: ImplicitFile<*>) : MediaAction<File> {
    override val method: TgMethod = TgMethod("uploadStickerFile")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<File>.defaultType: MediaContentType
        get() = MediaContentType.ImagePng
    override val MediaAction<File>.media: ImplicitFile<*>
        get() = pngSticker
    override val MediaAction<File>.dataField: String
        get() = "png_sticker"
}

fun uploadStickerFile(ba: ByteArray) = UploadStickerFileAction(ImplicitFile.InputFile(ba))
