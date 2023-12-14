@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerSetTitleAction(
    name: String,
    title: String,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerSetTitle")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name
        parameters["title"] = title
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerSetTitle(name: String, title: String) = SetStickerSetTitleAction(name, title)
