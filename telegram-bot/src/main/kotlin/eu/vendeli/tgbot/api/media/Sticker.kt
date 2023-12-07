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
import java.io.File

class SendStickerAction(sticker: ImplicitFile<*>) :
    MediaAction<Message>(),
    OptionsFeature<SendStickerAction, StickerOptions>,
    MarkupFeature<SendStickerAction> {
    override val method = TgMethod("sendSticker")
    override val returnType = getReturnType()
    override val OptionsFeature<SendStickerAction, StickerOptions>.options: StickerOptions
        get() = StickerOptions()
    override val inputFilePresence = sticker is ImplicitFile.InpFile

    init {
        parameters["sticker"] = sticker.file
    }
}

fun sticker(block: () -> String) = SendStickerAction(ImplicitFile.Str(block()))
fun sticker(ba: ByteArray) = SendStickerAction(ImplicitFile.InpFile(ba.toInputFile()))
fun sticker(file: File) = SendStickerAction(ImplicitFile.InpFile(file.toInputFile()))
fun sticker(file: InputFile) = SendStickerAction(ImplicitFile.InpFile(file))
