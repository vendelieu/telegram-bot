package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatPhoto(
    val smallFileId: String,
    val smallFileUniqueId: String,
    val bigFileId: String,
    val bigFileUniqueId: String,
)
