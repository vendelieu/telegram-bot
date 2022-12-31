@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoNoteOptions
import java.io.File

class SendVideoNoteAction(private val videoNote: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val method: TgMethod = TgMethod("sendVideoNote")
    override var options = VideoNoteOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.VideoMp4
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = videoNote
    override val MediaAction<Message>.dataField: String
        get() = "video_note"
}

fun videoNote(block: () -> String) = SendVideoNoteAction(ImplicitFile.FromString(block()))

fun videoNote(ba: ByteArray) = SendVideoNoteAction(ImplicitFile.FromByteArray(ba))

fun videoNote(file: File) = SendVideoNoteAction(ImplicitFile.FromFile(file))
