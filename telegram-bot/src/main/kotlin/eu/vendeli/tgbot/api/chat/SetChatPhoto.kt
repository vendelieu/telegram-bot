package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.http.*

class SetChatPhotoAction : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("setChatPhoto")

    init {
        setDataField("photo")
        setDefaultType(ContentType.Image.JPEG)
    }

    constructor(chatPhotoId: String) {
        setId(chatPhotoId)
    }

    constructor(chatPhoto: ByteArray) {
        setMedia(chatPhoto)
    }

    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun setChatPhoto(block: () -> String) = SetChatPhotoAction(block())

fun setChatPhoto(ba: ByteArray) = SetChatPhotoAction(ba)
