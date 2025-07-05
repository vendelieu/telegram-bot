package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun video(file: File) = video(file.toImplicitFile("video.mp4"))
