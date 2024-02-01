@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetCustomEmojiStickerSetThumbnailAction(
    name: String,
    customEmojiId: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setCustomEmojiStickerSetThumbnail")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
        if (customEmojiId != null) parameters["custom_emoji_id"] = customEmojiId.toJsonElement()
    }
}

inline fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? = null) =
    SetCustomEmojiStickerSetThumbnailAction(name, customEmojiId)
