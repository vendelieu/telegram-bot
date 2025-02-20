@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.handleImplicitFile
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonUnquotedLiteral

@TgAPI
@OptIn(ExperimentalSerializationApi::class)
class UploadStickerFileAction(
    userId: Long,
    sticker: InputFile,
    stickerFormat: StickerFormat,
) : SimpleAction<File>() {
    @TgAPI.Name("uploadStickerFile")
    override val method = "uploadStickerFile"
    override val returnType = getReturnType()

    init {
        handleImplicitFile(sticker.toImplicitFile(), "sticker")
        parameters["user_id"] = userId.toJsonElement()
        parameters["sticker_format"] = JsonUnquotedLiteral(stickerFormat.name.lowercase())
    }
}

/**
 * Use this method to upload a file with a sticker for later use in the createNewStickerSet, addStickerToSet, or replaceStickerInSet methods (the file can be used multiple times). Returns the uploaded File on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#uploadstickerfile)
 * @param userId User identifier of sticker file owner
 * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See https://core.telegram.org/stickers for technical requirements. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param stickerFormat Format of the sticker, must be one of "static", "animated", "video"
 * @returns [File]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun uploadStickerFile(userId: Long, sticker: InputFile, stickerFormat: StickerFormat) =
    UploadStickerFileAction(userId, sticker, stickerFormat)
