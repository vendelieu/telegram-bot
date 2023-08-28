@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
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

class SendAudioAction(private val audio: ImplicitFile<*>) :
    MediaAction<Message>,
    ActionState(),
    OptionsFeature<SendAudioAction, AudioOptions>,
    MarkupFeature<SendAudioAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendAudioAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendAudio")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendAudioAction, AudioOptions>.options: AudioOptions
        get() = AudioOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val MediaAction<Message>.inputFilePresence: Boolean
        get() = audio is ImplicitFile.InpFile

    init {
        parameters["audio"] = audio.file
    }
}

fun audio(block: () -> String) = SendAudioAction(ImplicitFile.Str(block()))
fun audio(ba: ByteArray) = SendAudioAction(ImplicitFile.InpFile(ba.toInputFile()))
fun audio(file: File) = SendAudioAction(ImplicitFile.InpFile(file.toInputFile()))
fun audio(file: InputFile) = SendAudioAction(ImplicitFile.InpFile(file))
