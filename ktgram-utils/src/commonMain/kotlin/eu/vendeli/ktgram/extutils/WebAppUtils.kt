package eu.vendeli.ktgram.extutils

import korlibs.crypto.HMAC
import net.thauvin.erik.urlencoder.UrlEncoderUtil.decode

/**
 * Function to check is web app data is safe.
 *
 * @param botToken bot token.
 * @param hash hash from webapp
 */
fun String.checkIsInitDataSafe(botToken: String, hash: String): Boolean {
    val secretKey = HMAC.hmacSHA256(botToken.encodeToByteArray(), "WebAppData".encodeToByteArray())
    val decodedData = decode(this)
        .split("&")
        .filterNot { it.startsWith("hash=") }
        .sorted()
        .joinToString("\n")

    return HMAC.hmacSHA256(secretKey.bytes, decodedData.encodeToByteArray()).hexLower == hash.lowercase()
}
