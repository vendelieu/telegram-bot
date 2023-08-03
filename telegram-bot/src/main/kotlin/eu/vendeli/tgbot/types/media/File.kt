package eu.vendeli.tgbot.types.media

data class File(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long? = null,
    val filePath: String? = null,
) {
    internal fun getDirectUrl(host: String, token: String): String? =
        filePath?.let { "https://%s/file/bot%s/%s".format(host, token, it) }
}
