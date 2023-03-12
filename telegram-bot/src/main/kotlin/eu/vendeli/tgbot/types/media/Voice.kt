package eu.vendeli.tgbot.types.media

data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Int,
    val mimeType: Sticker? = null,
    val fileSize: Long?,
)
