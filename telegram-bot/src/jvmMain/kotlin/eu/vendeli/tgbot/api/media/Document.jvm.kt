package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: File) = document(file.toImplicitFile())
