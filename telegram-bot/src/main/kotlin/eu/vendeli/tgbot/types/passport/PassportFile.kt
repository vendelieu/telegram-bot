package eu.vendeli.tgbot.types.passport

import java.time.Instant

data class PassportFile(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Int,
    val fileDate: Instant,
)
