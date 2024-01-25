@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetStickerSetThumbAction(
    name: String,
    thumbnail: StickerFile,
) : MediaAction<Boolean>() {
    override val method = TgMethod("setStickerSetThumbnail")
    override val returnType = getReturnType()
    override val idRefField = "user_id"
    override val inputFilePresence = thumbnail.data is ImplicitFile.InpFile

    init {
        parameters["thumbnail"] = thumbnail.data.file.toJsonElement()
        parameters["name"] = name.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerSetThumbnail(name: String, thumbnail: StickerFile) =
    SetStickerSetThumbAction(name, thumbnail)
