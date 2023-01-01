@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

class DeleteStickerFromSetAction(sticker: String) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteStickerFromSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sticker"] = sticker
    }
}

fun deleteStickerFromSet(sticker: String) = DeleteStickerFromSetAction(sticker)
