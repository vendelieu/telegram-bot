package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoNoteOptions

class SendVideoNoteAction :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val method: TgMethod = TgMethod("sendVideoNote")

    init {
        setDataField("video_note")
        setDefaultType(MediaContentType.VideoMp4)
    }

    constructor(videoNoteId: String) {
        setId(videoNoteId)
    }

    constructor(videoNote: ByteArray) {
        setMedia(videoNote)
    }

    override var options = VideoNoteOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun videoNote(block: () -> String) = SendVideoNoteAction(block())

fun videoNote(ba: ByteArray) = SendVideoNoteAction(ba)
