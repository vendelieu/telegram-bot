@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile.InpFile
import eu.vendeli.tgbot.types.internal.ImplicitFile.Str
import eu.vendeli.tgbot.types.internal.StickerFile.AttachedFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.getReturnType
import kotlin.collections.set

class AddStickerToSetAction(
    name: String,
    private val input: InputSticker,
) : MediaAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("addStickerToSet")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val MediaAction<Boolean>.isImplicit: Boolean
        get() = input.sticker.data is InpFile

    init {
        parameters["name"] = name
        val sticker = input.sticker
        parameters["sticker"] = if (sticker.data is InpFile) {
            val defaultName = "sticker.${sticker.contentType}"
            val filename = sticker.data.file.fileName.takeIf { it != "file" } ?: defaultName
            parameters[filename] = sticker.data

            AttachedFile(Str("attach://$filename"), sticker.stickerFormat, sticker.contentType)
        } else input
    }
}

fun addStickerToSet(name: String, input: InputSticker) = AddStickerToSetAction(name, input)
fun addStickerToSet(name: String, input: () -> InputSticker) = AddStickerToSetAction(name, input())
