@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetCustomEmojiStickerSetThumbnailAction(
    name: String,
    customEmojiId: String?,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setCustomEmojiStickerSetThumbnail")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name
        if (customEmojiId != null) parameters["custom_emoji_id"] = customEmojiId
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? = null) =
    SetCustomEmojiStickerSetThumbnailAction(name, customEmojiId)
