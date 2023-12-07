@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerKeywordsAction(
    sticker: String,
    keywords: List<String>?,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerKeywords")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker
        if (keywords != null) parameters["keywords"] = keywords
    }
}

fun setStickerKeywords(sticker: String, keywords: List<String>? = null) =
    SetStickerKeywordsAction(sticker, keywords)
