@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerSetThumbAction(
    name: String,
    userId: Long,
    private val thumb: StickerFile,
) : MediaAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setStickerSetThumb")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = thumb.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = thumb.file
    override val MediaAction<Boolean>.dataField: String
        get() = "thumb"

    init {
        parameters["name"] = name
        parameters["user_id"] = userId
    }
}

fun setStickerSetThumb(name: String, userId: Long, thumb: StickerFile) =
    SetStickerSetThumbAction(name, userId, thumb)
