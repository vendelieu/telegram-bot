package eu.vendeli.tgbot.types

data class PassportFile(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Int,
    val fileDate: Int
)
