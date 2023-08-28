@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoNoteOptions
import eu.vendeli.tgbot.types.internal.toInputFile
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
    override val MediaAction<Message>.inputFilePresence: Boolean
        get() = videoNote is ImplicitFile.InpFile

    init {
        parameters["video_note"] = videoNote.file
    }
}

fun videoNote(block: () -> String) = SendVideoNoteAction(ImplicitFile.Str(block()))
fun videoNote(ba: ByteArray) = SendVideoNoteAction(ImplicitFile.InpFile(ba.toInputFile()))
fun videoNote(input: InputFile) = SendVideoNoteAction(ImplicitFile.InpFile(input))
fun videoNote(file: File) = SendVideoNoteAction(ImplicitFile.InpFile(file.toInputFile()))
