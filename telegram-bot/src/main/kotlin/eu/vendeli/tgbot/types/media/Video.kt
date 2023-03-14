package eu.vendeli.tgbot.types.media

data class Video(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumbnail: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long?,
)
