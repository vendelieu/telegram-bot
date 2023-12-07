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
import eu.vendeli.tgbot.types.internal.options.VideoOptions
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendVideoAction(video: ImplicitFile<*>) :
    MediaAction<Message>(),
    OptionsFeature<SendVideoAction, VideoOptions>,
    MarkupFeature<SendVideoAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendVideoAction> {
    override val method = TgMethod("sendVideo")
    override val returnType = getReturnType()
    override val OptionsFeature<SendVideoAction, VideoOptions>.options: VideoOptions
        get() = VideoOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val inputFilePresence = video is ImplicitFile.InpFile

    init {
        parameters["video"] = video.file
    }
}

fun video(block: () -> String) = SendVideoAction(ImplicitFile.Str(block()))
fun video(ba: ByteArray) = SendVideoAction(ImplicitFile.InpFile(ba.toInputFile()))
fun video(file: File) = SendVideoAction(ImplicitFile.InpFile(file.toInputFile()))
fun video(file: InputFile) = SendVideoAction(ImplicitFile.InpFile(file))
