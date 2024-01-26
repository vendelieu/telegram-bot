package eu.vendeli.tgbot.types.internal

import kotlinx.serialization.Serializable
import java.io.File
import java.net.URLConnection

const val DEFAULT_FILENAME = "file"

@Serializable
data class InputFile(
    val data: ByteArray,
    val fileName: String = DEFAULT_FILENAME,
    val contentType: String = "text/plain",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

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

fun ByteArray.toInputFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = this.contentType,
) = InputFile(this, fileName, contentType)

fun File.toInputFile(
    fileName: String = name.ifEmpty { DEFAULT_FILENAME },
    contentType: String? = null,
) = this.readBytes().let { InputFile(it, fileName, contentType ?: it.contentType) }

@Suppress("NOTHING_TO_INLINE")
inline fun InputFile.toImplicitFile() = ImplicitFile.InpFile(this)

private val ByteArray.contentType: String
    get() = URLConnection.guessContentTypeFromStream(this.inputStream()) ?: "text/plain"
