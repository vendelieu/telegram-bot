@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType

class UploadStickerFileAction(private val sticker: StickerFile) : MediaAction<File>, ActionState() {
    override val method: TgMethod = TgMethod("uploadStickerFile")
    override val returnType = getReturnType()

    override val MediaAction<File>.defaultType: MediaContentType
        get() = MediaContentType.ImagePng
    override val MediaAction<File>.media: ImplicitFile<*>
        get() = sticker.file
    override val MediaAction<File>.dataField: String
        get() = "sticker"

    init {
        parameters["sticker_format"] = sticker.stickerFormat
    }
}

fun uploadStickerFile(sticker: StickerFile) = UploadStickerFileAction(sticker)
