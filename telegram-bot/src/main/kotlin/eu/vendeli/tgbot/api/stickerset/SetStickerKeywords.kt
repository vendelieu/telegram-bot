@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetStickerKeywordsAction(
    sticker: String,
    keywords: List<String>? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerKeywords")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
        if (keywords != null) parameters["keywords"] = keywords.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerKeywords(sticker: String, keywords: List<String>? = null) =
    SetStickerKeywordsAction(sticker, keywords)
