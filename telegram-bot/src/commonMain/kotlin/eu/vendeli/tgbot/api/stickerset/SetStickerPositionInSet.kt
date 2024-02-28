@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetStickerPositionInSetAction(sticker: String, position: Int) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerPositionInSet")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
        parameters["position"] = position.toJsonElement()
    }
}

/**
 * Use this method to move a sticker in a set created by the bot to a specific position. Returns True on success.
 * @param sticker Required 
 * @param position Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setstickerpositioninset
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setStickerPositionInSet(sticker: String, position: Int) = SetStickerPositionInSetAction(sticker, position)
