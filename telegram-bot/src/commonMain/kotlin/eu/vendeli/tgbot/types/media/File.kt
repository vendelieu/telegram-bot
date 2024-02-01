package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long? = null,
    val filePath: String? = null,
) {
    internal fun getDirectUrl(host: String, token: String): String? =
        filePath?.let { "https://$host/file/bot$token/$it" }
}
