package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.http.*

class SetStickerSetThumbAction(name: String, userId: Long, thumb: ByteArray) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("setStickerSetThumb")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        setDataField("thumb")
        setDefaultType(ContentType.MultiPart.FormData)
        setMedia(thumb)

        parameters["name"] = name
        parameters["user_id"] = userId
    }
}

fun setStickerSetThumb(name: String, userId: Long, thumb: ByteArray) = SetStickerSetThumbAction(name, userId, thumb)
