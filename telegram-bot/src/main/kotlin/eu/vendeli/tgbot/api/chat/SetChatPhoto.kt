@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SetChatPhotoAction(photo: ImplicitFile) : MediaAction<Boolean>() {
    override val method = TgMethod("setChatPhoto")
    override val returnType = getReturnType()

    init {
        handleImplicitFile(photo, "photo")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: ImplicitFile) = SetChatPhotoAction(file)
inline fun setChatPhoto(block: () -> String) = setChatPhoto(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: InputFile) = setChatPhoto(file.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(ba: ByteArray) = setChatPhoto(ba.toImplicitFile("image.jpg"))

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: File) = setChatPhoto(file.toImplicitFile("image.jpg"))
