package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoOptions

class SendVideoAction(video: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendVideoAction, VideoOptions>,
    MarkupFeature<SendVideoAction>,
    CaptionFeature<SendVideoAction> {
    override val method: TgMethod = TgMethod("sendVideo")

    init {
        when (video) {
            is ImplicitFile.FileId -> setId(video.file)
            is ImplicitFile.InputFile -> setMedia(video.file)
        }
        setDataField("video")
        setDefaultType(MediaContentType.VideoMp4)
    }

    override var options = VideoOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun video(block: () -> String) = SendVideoAction(ImplicitFile.FileId(block()))

fun video(ba: ByteArray) = SendVideoAction(ImplicitFile.InputFile(ba))
