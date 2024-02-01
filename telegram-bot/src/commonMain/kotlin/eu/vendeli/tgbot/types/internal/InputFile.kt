package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.DEFAULT_CONTENT_TYPE
import eu.vendeli.tgbot.utils.DEFAULT_FILENAME
import kotlinx.serialization.Serializable

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
