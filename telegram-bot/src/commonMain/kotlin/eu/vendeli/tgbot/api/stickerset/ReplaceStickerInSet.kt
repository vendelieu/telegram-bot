@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData

class ReplaceStickerInSetAction(
    name: String,
    oldSticker: String,
    sticker: InputSticker,
) : Action<Boolean>() {
    override val method = TgMethod("replaceStickerInSet")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
        parameters["old_sticker"] = oldSticker.toJsonElement()
        parameters["sticker"] = sticker.also {
            if (it.sticker is ImplicitFile.InpFile) {
                val inpSticker = it.sticker as ImplicitFile.InpFile
                multipartData += inpSticker.file.toPartData(inpSticker.file.fileName)

                it.sticker = "attach://${inpSticker.file.fileName}".toImplicitFile()
            }
        }.encodeWith(InputSticker.serializer())
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun replaceStickerInSet(name: String) = DeleteStickerSetAction(name)
