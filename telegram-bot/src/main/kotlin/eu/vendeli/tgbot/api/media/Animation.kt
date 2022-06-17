package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnimationOptions
import io.ktor.http.*

class SendAnimationAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendAnimationAction, AnimationOptions>,
    MarkupFeature<SendAnimationAction>,
    CaptionFeature<SendAnimationAction> {
    override val method: TgMethod = TgMethod("sendAnimation")

    init {
        setDataField("animation")
        setDefaultType(ContentType.Image.GIF)
    }

    constructor(animationId: String) {
        setId(animationId)
    }

    constructor(animation: ByteArray) {
        setMedia(animation)
    }

    override var options = AnimationOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun animation(block: () -> String) = SendAnimationAction(block())

fun animation(ba: ByteArray) = SendAnimationAction(ba)
