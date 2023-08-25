@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SetChatPhotoAction(private val photo: ImplicitFile<*>) : MediaAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setChatPhoto")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val MediaAction<Boolean>.inputFilePresence: Boolean
        get() = photo is ImplicitFile.InpFile

    init {
        parameters["photo"] = photo.file
    }
}

fun setChatPhoto(block: () -> String) = SetChatPhotoAction(ImplicitFile.Str(block()))

fun setChatPhoto(file: InputFile) = SetChatPhotoAction(ImplicitFile.InpFile(file))

fun setChatPhoto(ba: ByteArray) = SetChatPhotoAction(ImplicitFile.InpFile(ba.toInputFile()))

fun setChatPhoto(file: File) = SetChatPhotoAction(ImplicitFile.InpFile(file.toInputFile()))
