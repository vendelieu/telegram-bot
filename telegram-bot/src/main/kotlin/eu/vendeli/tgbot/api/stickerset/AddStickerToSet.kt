@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
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
        get() = input.sticker.data is ImplicitFile.InpFile

    init {
        // todo fix inputFile handling
        parameters["name"] = name
        parameters["sticker"] = input
    }
}

fun addStickerToSet(name: String, input: InputSticker) = AddStickerToSetAction(name, input)
