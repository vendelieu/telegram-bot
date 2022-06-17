package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CommonOptions
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
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun sticker(block: () -> String) = SendStickerAction(block())

fun sticker(ba: ByteArray) = SendStickerAction(ba)
