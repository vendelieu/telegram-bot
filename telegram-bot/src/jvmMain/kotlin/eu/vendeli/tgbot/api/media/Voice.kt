package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

@Suppress("NOTHING_TO_INLINE")
inline fun voice(file: File) = voice(file.toImplicitFile("voice.ogg"))
