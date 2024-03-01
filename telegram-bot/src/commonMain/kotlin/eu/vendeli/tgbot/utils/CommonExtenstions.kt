package eu.vendeli.tgbot.utils

import io.ktor.http.decodeURLQueryComponent
import korlibs.crypto.HMAC

fun String.checkIsInitDataSafe(botToken: String, hash: String): Boolean {
    val secretKey = HMAC.hmacSHA256(botToken.encodeToByteArray(), "WebAppData".encodeToByteArray())
    val decodedData = decodeURLQueryComponent()
        .split("&")
        .filterNot { it.startsWith("hash=") }
        .sorted()
        .joinToString("\n")

    return HMAC.hmacSHA256(secretKey.bytes, decodedData.encodeToByteArray()).hexLower == hash.lowercase()
}
