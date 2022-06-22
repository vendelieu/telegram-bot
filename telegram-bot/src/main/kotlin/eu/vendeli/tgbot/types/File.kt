package eu.vendeli.tgbot.types

data class File(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long? = null,
    val filePath: String? = null,
)
