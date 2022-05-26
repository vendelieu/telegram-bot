package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.types.internal.TgMethod
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

    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun setChatPhoto(block: () -> String) = SetChatPhotoAction(block())

fun setChatPhoto(ba: ByteArray) = SetChatPhotoAction(ba)
