@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
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

    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = input.sticker.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = input.sticker.file
    override val MediaAction<Boolean>.dataField: String
        get() = "sticker"

    init {
        parameters["name"] = name
        parameters["sticker"] = input
    }
}

fun addStickerToSet(name: String, input: InputSticker) = AddStickerToSetAction(name, input)
