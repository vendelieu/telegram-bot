@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerSetThumbAction(
    name: String,
    private val thumbnail: StickerFile,
) : MediaAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setStickerSetThumbnail")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val MediaAction<Boolean>.idRefField: String
        get() = "user_id"
    override val MediaAction<Boolean>.inputFilePresence: Boolean
        get() = thumbnail.data is ImplicitFile.InpFile

    init {
        parameters["thumbnail"] = thumbnail.data.file
        parameters["name"] = name
    }
}

fun setStickerSetThumbnail(name: String, thumbnail: StickerFile) =
    SetStickerSetThumbAction(name, thumbnail)
