@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.StickerSet
import eu.vendeli.tgbot.types.internal.TgMethod

class GetStickerSetAction(name: String) : SimpleAction<StickerSet> {
    override val method: TgMethod = TgMethod("getStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["name"] = name
    }
}

fun getStickerSet(name: String) = GetStickerSetAction(name)
