@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement

class UploadStickerFileAction(sticker: InputFile, stickerFormat: StickerFormat) : MediaAction<File>() {
    override val method = TgMethod("uploadStickerFile")
    override val returnType = getReturnType()
    override val idRefField: String = "user_id"

    init {
        handleImplicitFile(sticker.toImplicitFile(), "sticker")
        parameters["sticker_format"] = stickerFormat.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun uploadStickerFile(sticker: InputFile, stickerFormat: StickerFormat) =
    UploadStickerFileAction(sticker, stickerFormat)
