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
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
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

@Suppress("NOTHING_TO_INLINE")
inline fun audio(file: ImplicitFile) = SendAudioAction(file)

inline fun audio(block: () -> String) = audio(ImplicitFile.Str(block()))

@Suppress("NOTHING_TO_INLINE")
inline fun audio(ba: ByteArray) = audio(ImplicitFile.InpFile(ba.toInputFile("audio.mp3")))

@Suppress("NOTHING_TO_INLINE")
inline fun audio(file: File) = audio(ImplicitFile.InpFile(file.toInputFile("audio.mp3")))

@Suppress("NOTHING_TO_INLINE")
inline fun audio(file: InputFile) = audio(ImplicitFile.InpFile(file))

inline fun sendAudio(block: () -> String) = audio(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendAudio(file: ImplicitFile) = audio(file)
