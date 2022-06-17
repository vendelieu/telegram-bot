package eu.vendeli.tgbot.types

data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    val fileSize: Int?,
)
