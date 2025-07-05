package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun voice(file: File) = voice(file.toImplicitFile("voice.ogg"))
