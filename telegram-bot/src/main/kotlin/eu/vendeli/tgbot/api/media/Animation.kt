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
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendAnimationAction(animation: ImplicitFile<*>) :
    MediaAction<Message>(),
    OptionsFeature<SendAnimationAction, AnimationOptions>,
    MarkupFeature<SendAnimationAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendAnimationAction> {
    override val method = TgMethod("sendAnimation")
    override val returnType = getReturnType()
    override val OptionsFeature<SendAnimationAction, AnimationOptions>.options: AnimationOptions
        get() = AnimationOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val inputFilePresence = animation is ImplicitFile.InpFile

    init {
        parameters["animation"] = animation.file
    }
}

fun sendAnimation(block: () -> String) = animation(block)
fun sendAnimation(file: ImplicitFile<*>) = SendAnimationAction(file)
fun animation(block: () -> String) = SendAnimationAction(ImplicitFile.Str(block()))
fun animation(ba: ByteArray) = SendAnimationAction(ImplicitFile.InpFile(ba.toInputFile()))
fun animation(file: File) = SendAnimationAction(ImplicitFile.InpFile(file.toInputFile()))
fun animation(file: InputFile) = SendAnimationAction(ImplicitFile.InpFile(file))
