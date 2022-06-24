package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AudioOptions

class SendAudioAction(audio: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendAudioAction, AudioOptions>,
    MarkupFeature<SendAudioAction>,
    CaptionFeature<SendAudioAction> {
    override val method: TgMethod = TgMethod("sendAudio")

    init {
        when (audio) {
            is ImplicitFile.FileId -> setId(audio.file)
            is ImplicitFile.InputFile -> setMedia(audio.file)
        }
        setDataField("audio")
        setDefaultType(MediaContentType.Audio)
    }

    override var options = AudioOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun audio(block: () -> String) = SendAudioAction(ImplicitFile.FileId(block()))

fun audio(ba: ByteArray) = SendAudioAction(ImplicitFile.InputFile(ba))
