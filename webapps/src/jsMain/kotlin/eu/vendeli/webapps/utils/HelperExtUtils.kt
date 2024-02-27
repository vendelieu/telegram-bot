package eu.vendeli.webapps.utils

import eu.vendeli.webapps.core.webApp
import korlibs.crypto.HMAC

inline fun conditionalAction(
    sendDataAction: () -> String,
    answerWebAppQuery: (String) -> Unit,
    condition: () -> Boolean = { webApp.initDataUnsafe.queryId == null },
) {
    if (condition()) webApp.sendData(sendDataAction())
    else webApp.initDataUnsafe.queryId?.let(answerWebAppQuery)
}

@Suppress("UnusedReceiverParameter")
@JsName("decodeURLQueryComponent")
fun String.decodeURLQueryComponent(): String {
    return js("decodeURIComponent(this)").unsafeCast<String>()
}

fun String.isInitDataSafe(botToken: String, hash: String): Boolean {
    val secretKey = HMAC.hmacSHA256(botToken.encodeToByteArray(), "WebAppData".encodeToByteArray())
    val decodedData = decodeURLQueryComponent()
        .split("&")
        .filterNot { it.startsWith("hash=") }
        .sorted()
        .joinToString("\n")

    return HMAC.hmacSHA256(secretKey.bytes, decodedData.encodeToByteArray()).hexLower == hash.lowercase()
}

const val BG_COLOR = "bg_color"
const val SECONDARY_BG_COLOR = "secondary_bg_color"
