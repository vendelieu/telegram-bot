@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoNoteOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendVideoNoteAction(videoNote: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val method = TgMethod("sendVideoNote")
    override val returnType = getReturnType()
    override val options = VideoNoteOptions()

    init {
        handleImplicitFile(videoNote, "video_note")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(file: ImplicitFile) = SendVideoNoteAction(file)
inline fun videoNote(block: () -> String) = videoNote(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(ba: ByteArray) = videoNote(ba.toImplicitFile("note.mp4"))

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(input: InputFile) = videoNote(input.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(file: File) = videoNote(file.toImplicitFile("note.mp4"))

@Suppress("NOTHING_TO_INLINE")
inline fun sendVideoNote(file: ImplicitFile) = videoNote(file)
inline fun sendVideoNote(block: () -> String) = videoNote(block)
