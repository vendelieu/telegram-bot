@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetFileAction(
    fileId: String,
) : SimpleAction<File>() {
    @TgAPI.Name("getFile")
    override val method = "getFile"
    override val returnType = getReturnType()

    init {
        parameters["file_id"] = fileId.toJsonElement()
    }
}

/**
 * Use this method to get basic information about a file and prepare it for downloading. For the moment, bots can download files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile again.
 * Note: This function may not preserve the original file name and MIME type. You should save the file's MIME type and name (if available) when the File object is received.
 *
 * [Api reference](https://core.telegram.org/bots/api#getfile)
 * @param fileId File identifier to get information about
 * @returns [File]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getFile(fileId: String) = GetFileAction(fileId)
