package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod

class SetStickerSetThumbAction(name: String, userId: Long, thumb: StickerFile) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("setStickerSetThumb")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        setDataField("thumb")
        setDefaultType(thumb.contentType)
        setMedia(thumb.file)

        parameters["name"] = name
        parameters["user_id"] = userId
    }
}

fun setStickerSetThumb(name: String, userId: Long, thumb: StickerFile) =
    SetStickerSetThumbAction(name, userId, thumb)
