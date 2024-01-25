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
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import java.io.File

class SendVideoNoteAction(videoNote: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val method = TgMethod("sendVideoNote")
    override val returnType = getReturnType()
    override val options = VideoNoteOptions()
    override val inputFilePresence = videoNote is ImplicitFile.InpFile

    init {
        parameters["video_note"] = videoNote.file.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(file: ImplicitFile) = SendVideoNoteAction(file)
inline fun videoNote(block: () -> String) = videoNote(ImplicitFile.Str(block()))

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(ba: ByteArray) = videoNote(ImplicitFile.InpFile(ba.toInputFile("note.mp4")))

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(input: InputFile) = videoNote(ImplicitFile.InpFile(input))

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(file: File) = videoNote(ImplicitFile.InpFile(file.toInputFile("note.mp4")))

@Suppress("NOTHING_TO_INLINE")
inline fun sendVideoNote(file: ImplicitFile) = videoNote(file)
inline fun sendVideoNote(block: () -> String) = videoNote(block)
