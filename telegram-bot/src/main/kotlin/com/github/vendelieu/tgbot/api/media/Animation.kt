package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.interfaces.features.*
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.AnimationOptions
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
