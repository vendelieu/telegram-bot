package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.InlineKeyboardButton
import eu.vendeli.tgbot.types.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.LoginUrl
import eu.vendeli.tgbot.types.WebAppInfo

/**
 * Builder which is used to assemble the inline markup buttons.
 * [InlineKeyboardButton API](https://core.telegram.org/bots/api#inlinekeyboardbutton)
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class InlineKeyboardMarkupBuilder {
    private val buttons = mutableListOf<InlineKeyboardButton?>()

    /**
     * Url button
     *
     * @param name
     * @param value
     * @return [InlineKeyboardMarkupBuilder]
     */
    fun url(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, url = value()))
        return this
    }

    /**
     * CallbackData button
     *
     * @param name
     * @param value
     */
    fun callbackData(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, callbackData = value()))
        return this
    }

    /**
     * Web app info button
     *
     * @param name
     * @param value
     * @receiver
     * @return
     */
    fun webAppInfo(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, webApp = WebAppInfo(value())))
        return this
    }

    /**
     * Login url button
     *
     * @param name
     * @param value
     */
    fun loginUrl(name: String, value: () -> LoginUrl): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, loginUrl = value()))
        return this
    }

    /**
     * Switch inline query button
     *
     * @param name
     * @param value
     */
    fun switchInlineQuery(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, switchInlineQuery = value()))
        return this
    }

    /**
     * Callback game button
     *
     * @param name
     */
    fun callbackGame(name: String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, callbackGame = eu.vendeli.tgbot.types.CallbackGame))
        return this
    }

    /**
     * Pay button
     *
     * @param name
     */
    fun pay(name: String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, pay = true))
        return this
    }

    /**
     * Adds a line break, so that the following buttons will be on a new line.
     */
    fun newLine() {
        buttons.add(null)
    }

    /**
     * @see [newLine]
     */
    fun br() = newLine()

    /**
     * Callback button infix interface
     *
     * @param value
     */
    infix fun String.callback(value: String) = callbackData(this) { value }

    /**
     * Url button infix interface
     *
     * @param value
     */
    infix fun String.url(value: String) = url(this) { value }

    /**
     * Web app info button infix interface
     *
     * @param value
     */
    infix fun String.webAppInfo(value: String) = webAppInfo(this) { value }

    /**
     * Login url button infix interface
     *
     * @param value
     */
    infix fun String.loginUrl(value: LoginUrl) = loginUrl(this) { value }

    /**
     * Switch inline query button infix interface
     *
     * @param value
     */
    infix fun String.switchInlineQuery(value: String) = switchInlineQuery(this) { value }

    /**
     * The function that collects and returns [InlineKeyboardMarkup]
     *
     * @return
     */
    fun build(): List<List<InlineKeyboardButton>> {
        val builtButtons = mutableListOf<List<InlineKeyboardButton>>()

        val buttonsIterator = buttons.iterator()
        var currentLine: MutableList<InlineKeyboardButton> = mutableListOf()

        while (buttonsIterator.hasNext()) {
            val item = buttonsIterator.next()
            if (item == null) {
                builtButtons.add(currentLine)
                currentLine = mutableListOf()
                continue
            }
            currentLine.add(item)
        }
        if (currentLine.isNotEmpty()) builtButtons.add(currentLine)

        return builtButtons
    }
}

/**
 * Lightweight interface for [InlineKeyboardMarkupBuilder].
 * @param block
 */
fun inlineKeyboardMarkup(
    block: InlineKeyboardMarkupBuilder.() -> Unit,
) = InlineKeyboardMarkup(InlineKeyboardMarkupBuilder().apply(block).build())
