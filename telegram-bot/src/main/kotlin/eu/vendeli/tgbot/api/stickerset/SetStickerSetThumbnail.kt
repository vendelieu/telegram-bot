@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement

class SetStickerSetThumbnailAction(
    name: String,
    thumbnail: ImplicitFile? = null,
) : MediaAction<Boolean>() {
    override val method = TgMethod("setStickerSetThumbnail")
    override val returnType = getReturnType()
    override val idRefField = "user_id"

    init {
        if (thumbnail != null) handleImplicitFile(thumbnail, "thumbnail")
        parameters["name"] = name.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerSetThumbnail(name: String, thumbnail: ImplicitFile? = null) =
    SetStickerSetThumbnailAction(name, thumbnail)
