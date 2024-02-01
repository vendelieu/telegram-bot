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
import eu.vendeli.tgbot.types.internal.options.AudioOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendAudioAction(audio: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendAudioAction, AudioOptions>,
    MarkupFeature<SendAudioAction>,
    CaptionFeature<SendAudioAction> {
    override val method = TgMethod("sendAudio")
    override val returnType = getReturnType()
    override val options = AudioOptions()

    init {
        handleImplicitFile(audio, "audio")
    }
}

inline fun audio(file: ImplicitFile) = SendAudioAction(file)

inline fun audio(block: () -> String) = audio(block().toImplicitFile())

inline fun audio(ba: ByteArray) = audio(ba.toImplicitFile("audio.mp3"))

inline fun audio(file: File) = audio(file.toImplicitFile("audio.mp3"))

inline fun audio(file: InputFile) = audio(file.toImplicitFile())

inline fun sendAudio(block: () -> String) = audio(block)

inline fun sendAudio(file: ImplicitFile) = audio(file)
