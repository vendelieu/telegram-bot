package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.game.CallbackGame
import eu.vendeli.tgbot.types.inline.SwitchInlineQueryChosenChat
import eu.vendeli.tgbot.types.keyboard.CopyTextButton
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardButton
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.LoginUrl
import eu.vendeli.tgbot.types.keyboard.WebAppInfo
import kotlin.jvm.JvmName

/**
 * Builder which is used to assemble the inline markup buttons.
 * [InlineKeyboardButton API](https://core.telegram.org/bots/api#inlinekeyboardbutton)
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class InlineKeyboardMarkupBuilder : KeyboardBuilder<InlineKeyboardButton>() {
    override val buttons = mutableListOf<InlineKeyboardButton?>()

    /**
     * Url button
     */
    fun url(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, url = value())
        return this
    }

    /**
     * CallbackData button
     */
    fun callbackData(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, callbackData = value())
        return this
    }

    /**
     * Web app info button
     */
    fun webAppInfo(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, webApp = WebAppInfo(value()))
        return this
    }

    /**
     * Login url button
     */
    fun loginUrl(name: String, value: () -> LoginUrl): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, loginUrl = value())
        return this
    }

    /**
     * Switch inline query button
     */
    fun switchInlineQuery(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, switchInlineQuery = value())
        return this
    }

    /**
     * Switch inline query button for current chat
     */
    fun switchInlineQueryCurrentChat(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, switchInlineQueryCurrentChat = value())
        return this
    }

    /**
     * Switch inline query button for chosen chat
     */
    fun switchInlineQueryChosenChat(
        name: String,
        value: () -> SwitchInlineQueryChosenChat,
    ): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, switchInlineQueryChosenChat = value())
        return this
    }

    /**
     * Callback game button
     */
    fun callbackGame(name: String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, callbackGame = CallbackGame)
        return this
    }

    /**
     * Pay button
     */
    fun pay(name: String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, pay = true)
        return this
    }

    /**
     * CopyText button
     */
    fun copyText(name: String, copyText: () -> String): InlineKeyboardMarkupBuilder {
        buttons += InlineKeyboardButton(name, copyText = CopyTextButton(copyText()))
        return this
    }

    /**
     * Callback button infix method
     */

    inline infix fun String.callback(value: String) = callbackData(this) { value }

    /**
     * Url button infix method
     */

    inline infix fun String.url(value: String) = url(this) { value }

    /**
     * Web app info button infix method
     */

    inline infix fun String.webAppInfo(value: String) = webAppInfo(this) { value }

    /**
     * Login url button infix method
     */

    inline infix fun String.loginUrl(value: LoginUrl) = loginUrl(this) { value }

    /**
     * Switch inline query button infix method
     */

    inline infix fun String.switchInlineQuery(value: String) = switchInlineQuery(this) { value }

    /**
     * Switch inline query for current chat button infix method
     */

    inline infix fun String.switchInlineQueryCurrentChat(value: String) = switchInlineQueryCurrentChat(this) { value }

    /**
     * Switch inline query for chosen chat button infix method
     */

    inline infix fun String.switchInlineQueryChosenChat(value: SwitchInlineQueryChosenChat) =
        switchInlineQueryChosenChat(this) { value }

    /**
     * Pay button infix method
     */

    @JvmName("stringPay")
    inline fun String.pay() = pay(this)

    /**
     * CopyText button infix method
     */

    inline infix fun String.copyText(value: String) = copyText(this) { value }
}

/**
 * User-friendly DSL for creating [InlineKeyboardMarkup]. See [InlineKeyboardMarkupBuilder].
 */
fun inlineKeyboardMarkup(
    block: InlineKeyboardMarkupBuilder.() -> Unit,
) = InlineKeyboardMarkup(InlineKeyboardMarkupBuilder().apply(block).build())
