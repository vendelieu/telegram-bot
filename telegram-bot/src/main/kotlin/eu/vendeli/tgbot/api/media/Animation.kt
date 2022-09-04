package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnimationOptions

class SendAnimationAction(private val animation: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendAnimationAction, AnimationOptions>,
    MarkupFeature<SendAnimationAction>,
    CaptionFeature<SendAnimationAction> {
    override val method: TgMethod = TgMethod("sendAnimation")
    override var options = AnimationOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.ImageGif
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = animation
    override val MediaAction<Message>.dataField: String
        get() = "animation"
}

fun animation(block: () -> String) = SendAnimationAction(ImplicitFile.FileId(block()))

fun animation(ba: ByteArray) = SendAnimationAction(ImplicitFile.InputFile(ba))
