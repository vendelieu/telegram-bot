@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonUnquotedLiteral

@OptIn(ExperimentalSerializationApi::class)
class UploadStickerFileAction(sticker: InputFile, stickerFormat: StickerFormat) : MediaAction<File>() {
    override val method = TgMethod("uploadStickerFile")
    override val returnType = getReturnType()
    override val idRefField: String = "user_id"

    init {
        handleImplicitFile(sticker.toImplicitFile(), "sticker")
        parameters["sticker_format"] = JsonUnquotedLiteral(stickerFormat.name.lowercase())
    }
}

/**
 * Use this method to upload a file with a sticker for later use in the createNewStickerSet and addStickerToSet methods (the file can be used multiple times). Returns the uploaded File on success.
 * Api reference: https://core.telegram.org/bots/api#uploadstickerfile
 * @param userId User identifier of sticker file owner
 * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See https://core.telegram.org/stickers for technical requirements. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param stickerFormat Format of the sticker, must be one of "static", "animated", "video"
 * @returns [File]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun uploadStickerFile(sticker: InputFile, stickerFormat: StickerFormat) =
    UploadStickerFileAction(sticker, stickerFormat)
