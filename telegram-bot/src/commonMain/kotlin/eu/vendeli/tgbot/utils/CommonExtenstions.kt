package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
import io.ktor.http.decodeURLQueryComponent
import korlibs.crypto.HMAC
import kotlin.reflect.KClass

fun String.checkIsInitDataSafe(botToken: String, hash: String): Boolean {
    val secretKey = HMAC.hmacSHA256(botToken.encodeToByteArray(), "WebAppData".encodeToByteArray())
    val decodedData = decodeURLQueryComponent()
        .split("&")
        .filterNot { it.startsWith("hash=") }
        .sorted()
        .joinToString("\n")

    return HMAC.hmacSHA256(secretKey.bytes, decodedData.encodeToByteArray()).hexLower == hash.lowercase()
}

/**
 * Method to get given class instance through defined ClassManager.
 *
 * @param T output type.
 * @param kClass class to get instance.
 * @param initParams option to pass init parameters.
 */
@ExperimentalFeature
fun <T : Any> TelegramBot.getInstance(kClass: KClass<T>, vararg initParams: Any?): T =
    config.classManager.getInstance(kClass, initParams).cast()

/**
 * Helper function to paginate over a collection.
 *
 * @param T generic for paginating data.
 * @param nameSelector selector for button name.
 * @param callbackSelector selector for callback data.
 * @param paginationCallbackPattern pattern that will be used for pagination buttons.
 *  ex: `/start?page=%P` where `%P` will be replaced with page number.
 * @param currentPage current page
 * @param perPage elements per page.
 * @param perLine elements  per line.
 * @return InlineKeyboardMarkup.
 *
 * Example of Usage:
 * ```
 *     @CommandHandler(["/start"])
 *     suspend fun start(bot: TelegramBot, user: User) {
 *         message("Hello").markup(
 *             elements.joinToInlineKeyboard(
 *                 { "button $name" },
 *                 { "selector?id=$id" },
 *                 "/start?page=%P",
 *             )
 *         ).sendAsync(user, bot).getOrNull()?.also {
 *             bot.userData[user, "startPage"] = it.messageId
 *         }
 *     }
 *
 *     @CommandHandler.CallbackQuery(["/start"])
 *     suspend fun paginator(bot: TelegramBot, user: User, page: Int) {
 *         editMarkup(
 *             bot.userData.get<Long>(user.id, "startPage")!!
 *         ).markup(
 *             elements.joinToInlineKeyboard(
 *                 { "button $name" },
 *                 { "selector?id=$id" },
 *                 "/start?page=%P",
 *                 currentPage = page
 *             )
 *         ).send(user, bot)
 *     }
 * ```
 */
@ExperimentalFeature
fun <T> Collection<T>.joinToInlineKeyboard(
    nameSelector: T.() -> String,
    callbackSelector: T.() -> String,
    paginationCallbackPattern: String,
    currentPage: Int = 1,
    perPage: Int = 5,
    perLine: Int = 1,
): InlineKeyboardMarkup = inlineKeyboardMarkup {
    val elWindowEdge = if (currentPage == 1) {
        0..perPage
    } else {
        (currentPage - 1) * perPage..<(currentPage + 1) * perPage
    }
    val pageElWindow = toList().subList(elWindowEdge.first, elWindowEdge.last.takeIf { it <= size } ?: size)

    var lineElements = 0
    pageElWindow.forEach { i ->
        callbackData(i.nameSelector()) { i.callbackSelector() }
        lineElements++
        if (lineElements == perLine) {
            br()
            lineElements = 0
        }
    }

    br()
    if (elWindowEdge.first > 0) callbackData("<<") {
        paginationCallbackPattern.replace("%P", "${currentPage - 1}")
    }
    if (elWindowEdge.last < size) callbackData(">>") {
        paginationCallbackPattern.replace("%P", "${currentPage + 1}")
    }
}
