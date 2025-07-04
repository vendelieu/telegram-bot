package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun photo(file: File) = photo(file.toImplicitFile("photo.jpg"))
