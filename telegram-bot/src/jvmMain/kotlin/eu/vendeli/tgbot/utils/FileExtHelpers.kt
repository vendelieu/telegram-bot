package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.InputFile
import java.io.File
import java.net.URLConnection

fun ByteArray.toInputFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = this.contentType,
) = InputFile(this, fileName, contentType)

fun ByteArray.toImplicitFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = this.contentType,
) = toInputFile(fileName, contentType).toImplicitFile()

fun File.toInputFile(
    fileName: String = name.ifEmpty { DEFAULT_FILENAME },
    contentType: String? = null,
) = readBytes().let { InputFile(it, fileName, contentType ?: it.contentType) }

fun File.toImplicitFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = URLConnection.guessContentTypeFromName(name) ?: DEFAULT_CONTENT_TYPE,
) = toInputFile(fileName, contentType).toImplicitFile()

private val ByteArray.contentType: String
    get() = URLConnection.guessContentTypeFromStream(inputStream()) ?: DEFAULT_CONTENT_TYPE
