package eu.vendeli.tgbot.types.media

data class Document(
    val fileId: String,
    val fileUniqueId: String,
    val thumbnail: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
)
