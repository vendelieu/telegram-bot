@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SetChatPhotoAction(private val photo: ImplicitFile<*>) : MediaAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatPhoto")
    override val returnType = getReturnType()
    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = MediaContentType.ImageJpeg
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = photo
    override val MediaAction<Boolean>.dataField: String
        get() = "photo"
}

fun setChatPhoto(block: () -> String) = SetChatPhotoAction(ImplicitFile.FromString(block()))

fun setChatPhoto(ba: ByteArray) = SetChatPhotoAction(ImplicitFile.FromByteArray(ba))

fun setChatPhoto(file: File) = SetChatPhotoAction(ImplicitFile.FromFile(file))
