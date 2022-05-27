package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.interfaces.features.*
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.VoiceOptions
import io.ktor.http.*

class SendVoiceAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendVoiceAction, VoiceOptions>,
    MarkupFeature<SendVoiceAction>,
    CaptionFeature<SendVoiceAction> {
    override val method: TgMethod = TgMethod("sendVoice")

    init {
        setDataField("voice")
        setDefaultType(ContentType.Audio.OGG)
    }

    constructor(voiceId: String) {
        setId(voiceId)
    }

    constructor(voice: ByteArray) {
        setMedia(voice)
    }

    override var options = VoiceOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun voice(block: () -> String) = SendVoiceAction(block())

fun voice(ba: ByteArray) = SendVoiceAction(ba)
