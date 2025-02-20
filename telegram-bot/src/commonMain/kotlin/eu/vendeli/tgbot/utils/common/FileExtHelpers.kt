package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile

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

internal expect inline fun ByteArray.getContentType(): String
