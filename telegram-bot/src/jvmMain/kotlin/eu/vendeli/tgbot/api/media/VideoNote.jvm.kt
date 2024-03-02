package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(file: File) = videoNote(file.toImplicitFile("note.mp4"))
