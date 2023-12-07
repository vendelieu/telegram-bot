@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType

class UploadStickerFileAction(sticker: StickerFile) : MediaAction<File>() {
    override val method = TgMethod("uploadStickerFile")
    override val returnType = getReturnType()
    override val idRefField: String = "user_id"
    override val inputFilePresence = sticker.data is ImplicitFile.InpFile

    init {
        parameters["sticker"] = sticker.data.file
        parameters["sticker_format"] = sticker.stickerFormat.toString()
    }
}

fun uploadStickerFile(sticker: StickerFile) = UploadStickerFileAction(sticker)
