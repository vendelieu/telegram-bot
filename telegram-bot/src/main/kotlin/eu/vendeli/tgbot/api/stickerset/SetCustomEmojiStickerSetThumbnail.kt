@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetCustomEmojiStickerSetThumbnailAction(
    name: String,
    customEmojiId: String?,
) : SimpleAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setCustomEmojiStickerSetThumbnail")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        parameters["name"] = name
        if (customEmojiId != null) parameters["custom_emoji_id"] = customEmojiId
    }
}

fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? = null) =
    SetCustomEmojiStickerSetThumbnailAction(name, customEmojiId)
