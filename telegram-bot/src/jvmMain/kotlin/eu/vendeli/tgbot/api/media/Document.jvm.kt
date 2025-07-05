package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

inline fun document(file: File) = document(file.toImplicitFile())
