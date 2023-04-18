package eu.vendeli.tgbot.types.media

data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Int,
    val mimeType: String? = null,
    val fileSize: Long?,
)
