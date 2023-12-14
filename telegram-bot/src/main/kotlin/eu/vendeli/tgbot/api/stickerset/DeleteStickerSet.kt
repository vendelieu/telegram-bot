@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteStickerSetAction(name: String) : SimpleAction<Boolean>() {
    override val method = TgMethod("deleteStickerSet")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun deleteStickerSet(name: String) = DeleteStickerSetAction(name)
