@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.StickerOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendStickerAction(sticker: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendStickerAction, StickerOptions>,
    MarkupFeature<SendStickerAction> {
    override val method = TgMethod("sendSticker")
    override val returnType = getReturnType()
    override val options = StickerOptions()

    init {
        handleImplicitFile(sticker, "sticker")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(file: ImplicitFile) = SendStickerAction(file)
inline fun sticker(block: () -> String) = sticker(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(ba: ByteArray) = sticker(ba.toImplicitFile("sticker.webp"))

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(file: File) = sticker(file.toImplicitFile("sticker.webp"))

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(file: InputFile) = sticker(file.toImplicitFile())

inline fun sendSticker(block: () -> String) = sticker(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendSticker(file: ImplicitFile) = sticker(file)
