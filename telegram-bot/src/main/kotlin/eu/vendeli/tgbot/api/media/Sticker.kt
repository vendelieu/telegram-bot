@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.StickerOptions
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendStickerAction(private val sticker: ImplicitFile<*>) :
    MediaAction<Message>,
    ActionState(),
    OptionsFeature<SendStickerAction, StickerOptions>,
    MarkupFeature<SendStickerAction> {
    override val method: TgMethod = TgMethod("sendSticker")
    override val returnType = getReturnType()
    override val OptionsFeature<SendStickerAction, StickerOptions>.options: StickerOptions
        get() = StickerOptions()

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.ImageJpeg
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = sticker
    override val MediaAction<Message>.dataField: String
        get() = "sticker"
}

fun sticker(block: () -> String) = SendStickerAction(ImplicitFile.FromString(block()))

fun sticker(ba: ByteArray) = SendStickerAction(ImplicitFile.FromByteArray(ba))

fun sticker(file: File) = SendStickerAction(ImplicitFile.FromFile(file))
