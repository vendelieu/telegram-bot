package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.utils.common.toImplicitFile
import java.io.File

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(ba: ByteArray) = setChatPhoto(ba.toImplicitFile("image.jpg"))

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: File): SetChatPhotoAction = setChatPhoto(file.toImplicitFile("image.jpg"))
