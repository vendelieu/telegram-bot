package eu.vendeli.tgbot.types.media

data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Int,
    val thumbnail: PhotoSize? = null,
    val fileSize: Int?,
)
