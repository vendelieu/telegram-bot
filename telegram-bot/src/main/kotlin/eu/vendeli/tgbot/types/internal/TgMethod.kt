package eu.vendeli.tgbot.types.internal

data class TgMethod(val name: String) {
    internal fun getUrl(host: String, token: String) = "https://%s/bot%s/".format(host, token) + name
}
