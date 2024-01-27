@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement

class UploadStickerFileAction(sticker: StickerFile) : MediaAction<File>() {
    override val method = TgMethod("uploadStickerFile")
    override val returnType = getReturnType()
    override val idRefField: String = "user_id"
    init {
        handleImplicitFile(sticker.data, "sticker")
        parameters["sticker_format"] = sticker.stickerFormat.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun uploadStickerFile(sticker: StickerFile) = UploadStickerFileAction(sticker)
