package com.github.vendelieu.tgbot.api.stickerset

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.types.File
import com.github.vendelieu.tgbot.types.internal.TgMethod
import io.ktor.http.*

class UploadStickerFileAction(pngSticker: ByteArray) : MediaAction<File> {
    override val method: TgMethod = TgMethod("uploadStickerFile")

    init {
        setDataField("png_sticker")
        setDefaultType(ContentType.Image.PNG)
        setMedia(pngSticker)
    }

    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun uploadStickerFile(ba: ByteArray) = UploadStickerFileAction(ba)
