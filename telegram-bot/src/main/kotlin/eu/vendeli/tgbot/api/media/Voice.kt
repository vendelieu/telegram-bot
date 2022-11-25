package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VoiceOptions
import java.io.File

class SendVoiceAction(private val voice: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendVoiceAction, VoiceOptions>,
    MarkupFeature<SendVoiceAction>,
    CaptionFeature<SendVoiceAction> {
    override val method: TgMethod = TgMethod("sendVoice")
    override var options = VoiceOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.Voice
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = voice
    override val MediaAction<Message>.dataField: String
        get() = "voice"
}

fun voice(block: () -> String) = SendVoiceAction(ImplicitFile.FromString(block()))

fun voice(ba: ByteArray) = SendVoiceAction(ImplicitFile.FromByteArray(ba))

fun voice(file: File) = SendVoiceAction(ImplicitFile.FromFile(file))
