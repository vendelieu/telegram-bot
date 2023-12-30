package eu.vendeli.tgbot.types.media

data class PhotoSize(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val fileSize: Int? = null,
)
