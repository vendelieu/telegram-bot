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
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendAudioAction(audio: ImplicitFile<*>) :
    MediaAction<Message>(),
    OptionsFeature<SendAudioAction, AudioOptions>,
    MarkupFeature<SendAudioAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendAudioAction> {
    override val method = TgMethod("sendAudio")
    override val returnType = getReturnType()
    override val OptionsFeature<SendAudioAction, AudioOptions>.options: AudioOptions
        get() = AudioOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val inputFilePresence = audio is ImplicitFile.InpFile

    init {
        parameters["audio"] = audio.file
    }
}

fun sendAudio(block: () -> String) = audio(block)
fun sendAudio(file: ImplicitFile<*>) = SendAudioAction(file)
fun audio(block: () -> String) = SendAudioAction(ImplicitFile.Str(block()))
fun audio(ba: ByteArray) = SendAudioAction(ImplicitFile.InpFile(ba.toInputFile()))
fun audio(file: File) = SendAudioAction(ImplicitFile.InpFile(file.toInputFile()))
fun audio(file: InputFile) = SendAudioAction(ImplicitFile.InpFile(file))
