package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.CommonOptions
import io.ktor.http.*

class SendStickerAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendStickerAction, CommonOptions>,
    MarkupFeature<SendStickerAction> {
    override val method: TgMethod = TgMethod("sendSticker")

    init {
        setDataField("sticker")
        setDefaultType(ContentType.Image.JPEG)
    }

    constructor(stickerId: String) {
        setId(stickerId)
    }

    constructor(sticker: ByteArray) {
        setMedia(sticker)
    }

    override var options = CommonOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun sticker(block: () -> String) = SendStickerAction(block())

fun sticker(ba: ByteArray) = SendStickerAction(ba)
