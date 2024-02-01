package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile

const val DEFAULT_CONTENT_TYPE = "text/plain"
const val DEFAULT_FILENAME = "file"

fun ByteArray.toInputFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = DEFAULT_CONTENT_TYPE,
) = InputFile(this, fileName, contentType)

fun ByteArray.toImplicitFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = DEFAULT_CONTENT_TYPE,
) = toInputFile(fileName, contentType).toImplicitFile()

@Suppress("NOTHING_TO_INLINE")
inline fun InputFile.toImplicitFile() = ImplicitFile.InpFile(this)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toImplicitFile() = ImplicitFile.Str(this)
