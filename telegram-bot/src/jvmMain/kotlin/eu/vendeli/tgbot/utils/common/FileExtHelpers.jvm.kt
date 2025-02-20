package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.types.component.InputFile
import java.io.File
import java.net.URLConnection

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun ByteArray.getContentType() =
    URLConnection.guessContentTypeFromStream(inputStream()) ?: DEFAULT_CONTENT_TYPE

fun File.toInputFile(
    fileName: String = name.ifEmpty { DEFAULT_FILENAME },
    contentType: String? = null,
) = readBytes().let { InputFile(it, fileName, contentType ?: it.getContentType()) }

fun File.toImplicitFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = URLConnection.guessContentTypeFromName(name) ?: DEFAULT_CONTENT_TYPE,
) = toInputFile(fileName, contentType).toImplicitFile()
