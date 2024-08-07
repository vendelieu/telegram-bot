@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetStickerSetTitleAction(
    name: String,
    title: String,
) : SimpleAction<Boolean>() {
    override val method = "setStickerSetTitle"
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
        parameters["title"] = title.toJsonElement()
    }
}

/**
 * Use this method to set the title of a created sticker set. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setstickersettitle)
 * @param name Sticker set name
 * @param title Sticker set title, 1-64 characters
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun setStickerSetTitle(name: String, title: String) = SetStickerSetTitleAction(name, title)
