@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AudioOptions
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
    override val method: TgMethod = TgMethod("sendAudio")
    override val returnType = getReturnType()
    override var options = AudioOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.Audio
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = audio
    override val MediaAction<Message>.dataField: String
        get() = "audio"
}

fun audio(block: () -> String) = SendAudioAction(ImplicitFile.FromString(block()))

fun audio(ba: ByteArray) = SendAudioAction(ImplicitFile.FromByteArray(ba))

fun audio(file: File) = SendAudioAction(ImplicitFile.FromFile(file))
