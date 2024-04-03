package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.media.PhotoSize
import kotlinx.serialization.Serializable

@Serializable
data class SharedUser(
    val userId: Long,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val photo: List<PhotoSize>? = null,
)
