@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SetChatPhotoAction(photo: ImplicitFile<*>) : MediaAction<Boolean>() {
    override val method = TgMethod("setChatPhoto")
    override val returnType = getReturnType()
    override val inputFilePresence = photo is ImplicitFile.InpFile

    init {
        parameters["photo"] = photo.file
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: ImplicitFile<*>) = SetChatPhotoAction(file)
inline fun setChatPhoto(block: () -> String) = setChatPhoto(ImplicitFile.Str(block()))

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: InputFile) = setChatPhoto(ImplicitFile.InpFile(file))

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(ba: ByteArray) = setChatPhoto(ImplicitFile.InpFile(ba.toInputFile()))

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: File) = setChatPhoto(ImplicitFile.InpFile(file.toInputFile()))
