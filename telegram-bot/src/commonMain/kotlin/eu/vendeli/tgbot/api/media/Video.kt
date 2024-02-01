@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendVideoAction(video: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendVideoAction, VideoOptions>,
    MarkupFeature<SendVideoAction>,
    CaptionFeature<SendVideoAction> {
    override val method = TgMethod("sendVideo")
    override val returnType = getReturnType()
    override val options = VideoOptions()

    init {
        handleImplicitFile(video, "video")
    }
}

inline fun video(file: ImplicitFile) = SendVideoAction(file)
inline fun video(block: () -> String) = video(block().toImplicitFile())

inline fun video(ba: ByteArray) = video(ba.toImplicitFile("video.mp4"))

inline fun video(file: File) = video(file.toImplicitFile("video.mp4"))

inline fun video(file: InputFile) = video(file.toImplicitFile())
inline fun sendVideo(block: () -> String) = video(block)

inline fun sendVideo(file: ImplicitFile) = video(file)
