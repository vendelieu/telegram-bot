package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.DEFAULT_CONTENT_TYPE
import eu.vendeli.tgbot.utils.common.DEFAULT_FILENAME
import kotlinx.serialization.Serializable

/**
 * This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual way that files are uploaded via the browser.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputfile)
 *
 */
@Serializable
data class InputFile(
    val data: ByteArray,
    val fileName: String = DEFAULT_FILENAME,
    val contentType: String = DEFAULT_CONTENT_TYPE,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as InputFile

        if (!data.contentEquals(other.data)) return false
        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }
}
