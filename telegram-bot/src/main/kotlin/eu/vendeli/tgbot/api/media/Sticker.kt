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
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import java.io.File

class SendStickerAction(sticker: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendStickerAction, StickerOptions>,
    MarkupFeature<SendStickerAction> {
    override val method = TgMethod("sendSticker")
    override val returnType = getReturnType()
    override val options = StickerOptions()
    override val inputFilePresence = sticker is ImplicitFile.InpFile

    init {
        parameters["sticker"] = sticker.file.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(file: ImplicitFile) = SendStickerAction(file)
inline fun sticker(block: () -> String) = sticker(ImplicitFile.Str(block()))

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(ba: ByteArray) = sticker(ImplicitFile.InpFile(ba.toInputFile("sticker.webp")))

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(file: File) = sticker(ImplicitFile.InpFile(file.toInputFile("sticker.webp")))

@Suppress("NOTHING_TO_INLINE")
inline fun sticker(file: InputFile) = sticker(ImplicitFile.InpFile(file))

inline fun sendSticker(block: () -> String) = sticker(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendSticker(file: ImplicitFile) = sticker(file)
