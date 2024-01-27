package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.DEFAULT_FILENAME
import eu.vendeli.tgbot.types.internal.ImplicitFile
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
    contentType: String = URLConnection.guessContentTypeFromName(name) ?: "text/plain",
) = toInputFile(fileName, contentType).toImplicitFile()

@Suppress("NOTHING_TO_INLINE")
inline fun InputFile.toImplicitFile() = ImplicitFile.InpFile(this)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toImplicitFile() = ImplicitFile.Str(this)

private val ByteArray.contentType: String
    get() = URLConnection.guessContentTypeFromStream(inputStream()) ?: "text/plain"
