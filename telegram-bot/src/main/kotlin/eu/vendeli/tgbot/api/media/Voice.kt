package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VoiceOptions

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
        setDefaultType(MediaContentType.Voice)
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
