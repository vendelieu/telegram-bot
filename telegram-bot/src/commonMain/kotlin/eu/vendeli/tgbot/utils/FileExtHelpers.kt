package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile

internal expect inline fun ByteArray.getContentType(): String

fun ByteArray.toInputFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = getContentType(),
) = InputFile(this, fileName, contentType)

fun ByteArray.toImplicitFile(
    fileName: String = DEFAULT_FILENAME,
    contentType: String = getContentType(),
) = toInputFile(fileName, contentType).toImplicitFile()

@Suppress("NOTHING_TO_INLINE")
inline fun InputFile.toImplicitFile() = ImplicitFile.InpFile(this)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toImplicitFile() = ImplicitFile.Str(this)
