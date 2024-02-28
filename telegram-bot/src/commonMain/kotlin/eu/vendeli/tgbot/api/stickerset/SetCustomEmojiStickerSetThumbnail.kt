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

/**
 * Use this method to set the thumbnail of a custom emoji sticker set. Returns True on success.
 * @param name Required 
 * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setcustomemojistickersetthumbnail
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? = null) =
    SetCustomEmojiStickerSetThumbnailAction(name, customEmojiId)
