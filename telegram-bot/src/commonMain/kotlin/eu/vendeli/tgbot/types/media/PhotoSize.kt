package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

@Serializable
data class PhotoSize(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val fileSize: Int? = null,
)
