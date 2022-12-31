@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod

class SetStickerSetThumbAction(name: String, userId: Long, private val thumb: StickerFile) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("setStickerSetThumb")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = thumb.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = thumb.toImplicitFile()
    override val MediaAction<Boolean>.dataField: String
        get() = "thumb"

    init {
        parameters["name"] = name
        parameters["user_id"] = userId
    }
}

fun setStickerSetThumb(name: String, userId: Long, thumb: StickerFile) =
    SetStickerSetThumbAction(name, userId, thumb)
