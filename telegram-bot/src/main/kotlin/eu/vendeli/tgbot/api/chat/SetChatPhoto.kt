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

fun setChatPhoto(block: () -> String) = SetChatPhotoAction(ImplicitFile.Str(block()))

fun setChatPhoto(file: InputFile) = SetChatPhotoAction(ImplicitFile.InpFile(file))

fun setChatPhoto(ba: ByteArray) = SetChatPhotoAction(ImplicitFile.InpFile(ba.toInputFile()))

fun setChatPhoto(file: File) = SetChatPhotoAction(ImplicitFile.InpFile(file.toInputFile()))
