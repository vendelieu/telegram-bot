package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun videoNote(file: File) = videoNote(file.toImplicitFile("note.mp4"))
