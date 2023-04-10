package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.interfaces.KeyboardBuilder
import eu.vendeli.tgbot.types.game.CallbackGame
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardButton
import eu.vendeli.tgbot.types.keyboard.InlineKeyboardMarkup
import eu.vendeli.tgbot.types.keyboard.LoginUrl
import eu.vendeli.tgbot.types.keyboard.WebAppInfo

/**
 * Builder which is used to assemble the inline markup buttons.
 * [InlineKeyboardButton API](https://core.telegram.org/bots/api#inlinekeyboardbutton)
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class InlineKeyboardMarkupBuilder : KeyboardBuilder<InlineKeyboardButton>() {
    override val buttons = mutableListOf<InlineKeyboardButton?>()

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
     * @param value Web app url
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
     * Switch inline query button for current chat
     *
     * @param name
     * @param value
     */
    fun switchInlineQueryCurrentChat(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, switchInlineQueryCurrentChat = value()))
        return this
    }

    /**
     * Callback game button
     *
     * @param name
     */
    fun callbackGame(name: String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, callbackGame = CallbackGame))
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
     * Switch inline query for current chat button infix interface
     *
     * @param value
     */
    infix fun String.switchInlineQueryCurrentChat(value: String) = switchInlineQueryCurrentChat(this) { value }
}

/**
 * User-friendly DSL for creating [InlineKeyboardMarkup]. See [InlineKeyboardMarkupBuilder].
 */
fun inlineKeyboardMarkup(
    block: InlineKeyboardMarkupBuilder.() -> Unit,
) = InlineKeyboardMarkup(InlineKeyboardMarkupBuilder().apply(block).build())
