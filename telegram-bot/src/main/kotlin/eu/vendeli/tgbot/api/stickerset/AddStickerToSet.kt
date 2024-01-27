@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlin.collections.set

class AddStickerToSetAction(
    name: String,
    input: InputSticker,
) : MediaAction<Boolean>() {
    override val method = TgMethod("addStickerToSet")
    override val returnType = getReturnType()
    override val idRefField = "user_id"

    init {
        parameters["name"] = name.toJsonElement()
        parameters["sticker"] = input.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun addStickerToSet(name: String, input: InputSticker) = AddStickerToSetAction(name, input)

inline fun addStickerToSet(name: String, input: () -> InputSticker) = AddStickerToSetAction(name, input())
