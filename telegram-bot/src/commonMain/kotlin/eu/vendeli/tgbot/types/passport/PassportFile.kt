package eu.vendeli.tgbot.types.passport

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PassportFile(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Int,
    @Serializable(InstantSerializer::class)
    val fileDate: Instant,
)
