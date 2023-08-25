@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.getReturnType

class UploadStickerFileAction(userId: Long, private val sticker: StickerFile) : MediaAction<File>, ActionState() {
    override val TgAction<File>.method: TgMethod
        get() = TgMethod("uploadStickerFile")
    override val TgAction<File>.returnType: Class<File>
        get() = getReturnType()
    override val MediaAction<File>.inputFilePresence: Boolean
        get() = sticker.data is ImplicitFile.InpFile

    init {
        parameters["user_id"] = userId
        parameters["sticker"] = sticker.data.file
        parameters["sticker_format"] = sticker.stickerFormat.toString()
    }
}

fun uploadStickerFile(userId: Long, sticker: StickerFile) = UploadStickerFileAction(userId, sticker)
fun uploadStickerFile(user: User, sticker: StickerFile) = UploadStickerFileAction(user.id, sticker)
