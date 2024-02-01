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
import eu.vendeli.tgbot.types.internal.options.AnimationOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SendAnimationAction(animation: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendAnimationAction, AnimationOptions>,
    MarkupFeature<SendAnimationAction>,
    CaptionFeature<SendAnimationAction> {
    override val method = TgMethod("sendAnimation")
    override val returnType = getReturnType()
    override val options = AnimationOptions()

    init {
        handleImplicitFile(animation, "animation")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun animation(file: ImplicitFile) = SendAnimationAction(file)
inline fun animation(block: () -> String) = animation(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun animation(ba: ByteArray) = animation(ba.toImplicitFile("image.gif"))

@Suppress("NOTHING_TO_INLINE")
inline fun animation(file: InputFile) = animation(file.toImplicitFile())
inline fun sendAnimation(block: () -> String) = animation(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendAnimation(file: ImplicitFile) = animation(file)
