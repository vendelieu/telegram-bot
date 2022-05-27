package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.interfaces.features.*
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.VideoOptions
import io.ktor.http.*

class SendVideoAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendVideoAction, VideoOptions>,
    MarkupFeature<SendVideoAction>,
    CaptionFeature<SendVideoAction> {
    override val method: TgMethod = TgMethod("sendVideo")

    init {
        setDataField("video")
        setDefaultType(ContentType.Video.MP4)
    }

    constructor(videoId: String) {
        setId(videoId)
    }

    constructor(video: ByteArray) {
        setMedia(video)
    }

    override var options = VideoOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun video(block: () -> String) = SendVideoAction(block())

fun video(ba: ByteArray) = SendVideoAction(ba)
