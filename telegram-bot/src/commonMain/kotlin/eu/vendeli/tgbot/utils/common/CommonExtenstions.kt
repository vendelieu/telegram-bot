package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.ctx.InputListener
import eu.vendeli.tgbot.interfaces.helper.ExceptionHandler
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.Link
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.utils.builders.inlineKeyboardMarkup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

/**
 * Runs exception handler loop.
 *
 * @param dispatcher Dispatcher used for running handler.
 * @param delay Delay after each handling iteration.
 * @param block Handling action.
 */
@ExperimentalFeature
fun TgUpdateHandler.runExceptionHandler(
    dispatcher: CoroutineDispatcher = PROCESSING_DISPATCHER,
    delay: Long = 100,
    block: ExceptionHandler,
) = handlerScope.launch(dispatcher) {
    caughtExceptions.consumeEach { fUpd ->
        block.handle(fUpd.exception, fUpd.update, bot)
        delay.takeIf { it > 0 }?.let { delay(it) }
    }
}

/**
 * Set chain for input listening.
 *
 * Basically uses chain full qualified name as a chain id.
 *
 * @param T given ChainLink
 * @param user The user for whom it will be set.
 * @param firstLink The First link that will be processed (it doesn't have to be the first link in the chain, feel free to set up any of).
 */
fun <T : Link<*>> InputListener.setChain(user: User, firstLink: T) = set(user, firstLink::class.fqName)

/**
 * Method to get given class instance through defined ClassManager.
 *
 * @param T output type.
 * @param kClass class to get instance.
 * @param initParams option to pass init parameters.
 */
@ExperimentalFeature
fun <T : Any> TelegramBot.getInstance(kClass: KClass<T>, vararg initParams: Any?): T? =
    config.classManager.getInstance(kClass, initParams).safeCast()

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
 *         ).sendReturning(user, bot).getOrNull()?.also {
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

/**
 * Escape text for given parse mode.
 *
 * See [escapeHTML], [escapeMarkdown], [escapeMarkdownV2].
 *
 * @since 7.7.0
 */
inline fun String.escapeFormatting(mode: ParseMode): String = when (mode) {
    ParseMode.Markdown -> escapeMarkdown()
    ParseMode.MarkdownV2 -> escapeMarkdownV2()
    ParseMode.HTML -> escapeHTML()
}

private val markdownV1EscapeList = setOf('_', '*', '`', '[')

/**
 * Escapes text for markdown v1.
 *
 * [MarkdownV1 escape rules](https://core.telegram.org/bots/api#markdownv2-style)
 *
 * @since 7.7.0
 */
fun String.escapeMarkdown() = buildString {
    forEach {
        if (it in markdownV1EscapeList) append("\\$it") else append(it)
    }
}

private val markdownV2EscapeList = setOf(
    '_',
    '*',
    '[',
    ']',
    '(',
    ')',
    '~',
    '`',
    '>',
    '#',
    '+',
    '-',
    '=',
    '|',
    '{',
    '}',
    '.',
    '!',
)

/**
 * Escapes text for markdown v2.
 *
 * [MarkdownV2 escape rules](https://core.telegram.org/bots/api#markdownv2-style)
 * @since 7.7.0
 */
fun String.escapeMarkdownV2() = buildString {
    forEach {
        if (it in markdownV2EscapeList) append("\\$it") else append(it)
    }
}

private val HTMLEscapeMap = mapOf(
    '&' to "&amp;",
    '<' to "&lt;",
    '>' to "&gt;",
)

/**
 * Escapes text for html.
 *
 * [HTML escape rules](https://core.telegram.org/bots/api#html-style)
 * @since 7.7.0
 */
fun String.escapeHTML() = buildString {
    forEach { ch ->
        HTMLEscapeMap[ch]?.let { append(it) } ?: append(ch)
    }
}
