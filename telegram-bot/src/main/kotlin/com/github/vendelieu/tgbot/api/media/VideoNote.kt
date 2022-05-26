package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.VideoNoteOptions
import io.ktor.http.*

class SendVideoNoteAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val method: TgMethod = TgMethod("sendVideoNote")

    init {
        setDataField("video_note")
        setDefaultType(ContentType.Video.MP4)
    }

    constructor(videoNoteId: String) {
        setId(videoNoteId)
    }

    constructor(videoNote: ByteArray) {
        setMedia(videoNote)
    }

    override var options = VideoNoteOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun videoNote(block: () -> String) = SendVideoNoteAction(block())

fun videoNote(ba: ByteArray) = SendVideoNoteAction(ba)
