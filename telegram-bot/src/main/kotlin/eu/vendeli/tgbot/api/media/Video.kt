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
import eu.vendeli.tgbot.types.internal.options.VideoOptions
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendVideoAction(private val video: ImplicitFile<*>) :
    MediaAction<Message>,
    ActionState(),
    OptionsFeature<SendVideoAction, VideoOptions>,
    MarkupFeature<SendVideoAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendVideoAction> {
    override val method: TgMethod = TgMethod("sendVideo")
    override val returnType = getReturnType()
    override val OptionsFeature<SendVideoAction, VideoOptions>.options: VideoOptions
        get() = VideoOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.VideoMp4
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = video
    override val MediaAction<Message>.dataField: String
        get() = "video"
}

fun video(block: () -> String) = SendVideoAction(ImplicitFile.FromString(block()))

fun video(ba: ByteArray) = SendVideoAction(ImplicitFile.FromByteArray(ba))

fun video(file: File) = SendVideoAction(ImplicitFile.FromFile(file))
