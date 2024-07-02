package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PaidMediaOptions
import eu.vendeli.tgbot.types.media.InputPaidMedia
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFileGroup
import eu.vendeli.tgbot.utils.toJsonElement

class SendPaidMediaAction(starCount: Int, inputPaidMedia: List<InputPaidMedia>) :
    MediaAction<Message>(),
    OptionsFeature<SendPaidMediaAction, PaidMediaOptions>,
    CaptionFeature<SendPaidMediaAction>,
    MarkupFeature<SendPaidMediaAction> {
    override val method = TgMethod("sendPaidMedia")
    override val returnType = getReturnType()
    override val options = PaidMediaOptions()

    init {
        parameters["star_count"] = starCount.toJsonElement()
        handleImplicitFileGroup(inputPaidMedia)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun sendPaidMedia(starCount: Int, media: List<InputPaidMedia>) = SendPaidMediaAction(starCount, media)

@Suppress("NOTHING_TO_INLINE")
fun sendPaidMedia(starCount: Int, media: ListingBuilder<InputPaidMedia>.() -> Unit) =
    SendPaidMediaAction(starCount, ListingBuilder.build(media))

@Suppress("NOTHING_TO_INLINE")
inline fun sendPaidMedia(starCount: Int, vararg media: InputPaidMedia) = SendPaidMediaAction(starCount, media.asList())
