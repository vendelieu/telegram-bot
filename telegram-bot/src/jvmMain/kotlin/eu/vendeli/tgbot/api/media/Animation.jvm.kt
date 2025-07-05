package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun animation(file: File) = animation(file.toImplicitFile("image.gif"))
