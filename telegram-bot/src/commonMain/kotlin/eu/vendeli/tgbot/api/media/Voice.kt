@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VoiceOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendVoiceAction(voice: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendVoiceAction, VoiceOptions>,
    MarkupFeature<SendVoiceAction>,
    CaptionFeature<SendVoiceAction> {
    override val method = TgMethod("sendVoice")
    override val returnType = getReturnType()
    override val options = VoiceOptions()

    init {
        handleImplicitFile(voice, "voice")
    }
}

inline fun voice(file: ImplicitFile) = SendVoiceAction(file)
inline fun voice(block: () -> String) = voice(block().toImplicitFile())

inline fun voice(file: InputFile) = voice(file.toImplicitFile())

inline fun voice(ba: ByteArray) = voice(ba.toImplicitFile("voice.ogg"))

inline fun voice(file: File) = voice(file.toImplicitFile("voice.ogg"))

inline fun sendVoice(block: () -> String) = voice(block)

inline fun sendVoice(file: ImplicitFile) = voice(file)
