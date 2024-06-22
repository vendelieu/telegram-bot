package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
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


fun <T> Collection<T>.joinToInlineKeyboard(
    nameSelector: T.() -> String,
    callbackSelector: T.() -> String,
    currentPage: Int = 1,
    perPage: Int = 5,
    perLine: Int = 1,
): InlineKeyboardMarkup = inlineKeyboardMarkup {
    val elWindowLeftEdge = if (currentPage == 1) 0 else ((currentPage - 1) * perPage - 1)
    val elWindowRightEdge = if (currentPage == 1) perPage else (currentPage * perPage - 1)
    val pageElWindow = toList().subList(elWindowLeftEdge, elWindowRightEdge.takeIf { it <= size } ?: size)

    pageElWindow.forEachIndexed { idx, i ->
        callbackData(i.nameSelector()) { i.callbackSelector() }
        if (idx == perLine - 1) br()
    }
}
