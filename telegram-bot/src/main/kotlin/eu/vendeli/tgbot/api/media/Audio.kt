package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AudioOptions

class SendAudioAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendAudioAction, AudioOptions>,
    MarkupFeature<SendAudioAction>,
    CaptionFeature<SendAudioAction> {
    override val method: TgMethod = TgMethod("sendAudio")

    init {
        setDataField("audio")
        setDefaultType(MediaContentType.Audio)
    }

    constructor(audioId: String) {
        setId(audioId)
    }

    constructor(audio: ByteArray) {
        setMedia(audio)
    }

    override var options = AudioOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun audio(block: () -> String) = SendAudioAction(block())

fun audio(ba: ByteArray) = SendAudioAction(ba)
