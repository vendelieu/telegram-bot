@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType

class UploadStickerFileAction(private val sticker: StickerFile) : MediaAction<File>, ActionState() {
    override val TgAction<File>.method: TgMethod
        get() = TgMethod("uploadStickerFile")
    override val TgAction<File>.returnType: Class<File>
        get() = getReturnType()
    override val MediaAction<File>.isImplicit: Boolean
        get() = sticker.data is ImplicitFile.InpFile

    init {
        parameters["sticker"] = sticker.data.file
        parameters["sticker_format"] = sticker.stickerFormat
    }
}

fun uploadStickerFile(sticker: StickerFile) = UploadStickerFileAction(sticker)
