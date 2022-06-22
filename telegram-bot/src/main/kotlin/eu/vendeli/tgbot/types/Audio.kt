package eu.vendeli.tgbot.types

data class Audio(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Int,
    val performer: String? = null,
    val title: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val thumb: PhotoSize?,
)
