@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.StickerSet
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetStickerSetAction(name: String) : SimpleAction<StickerSet>() {
    override val method = TgMethod("getStickerSet")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
    }
}

/**
 * Use this method to get a sticker set. On success, a StickerSet object is returned.
 * @param name Required 
 * @returns [StickerSet]
 * Api reference: https://core.telegram.org/bots/api#getstickerset
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getStickerSet(name: String) = GetStickerSetAction(name)
