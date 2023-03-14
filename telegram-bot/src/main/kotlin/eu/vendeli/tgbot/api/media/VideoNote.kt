@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoNoteOptions
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendVideoNoteAction(private val videoNote: ImplicitFile<*>) :
    MediaAction<Message>,
    ActionState(),
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendVideoNote")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendVideoNoteAction, VideoNoteOptions>.options: VideoNoteOptions
        get() = VideoNoteOptions()

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
