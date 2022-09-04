package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatPhotoAction(private val photo: ImplicitFile<*>) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("setChatPhoto")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = MediaContentType.ImageJpeg
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = photo
    override val MediaAction<Boolean>.dataField: String
        get() = "photo"
}

fun setChatPhoto(block: () -> String) = SetChatPhotoAction(ImplicitFile.FileId(block()))

fun setChatPhoto(ba: ByteArray) = SetChatPhotoAction(ImplicitFile.InputFile(ba))
