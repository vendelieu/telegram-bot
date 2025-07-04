package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun audio(file: File) = audio(file.toImplicitFile("audio.mp3"))
