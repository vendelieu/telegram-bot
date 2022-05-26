package com.github.vendelieu.tgbot.utils

import com.github.vendelieu.tgbot.types.InlineKeyboardButton
import com.github.vendelieu.tgbot.types.InlineKeyboardMarkup
import com.github.vendelieu.tgbot.types.WebAppInfo

/**
 * Builder which is used to assemble the markup buttons.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class InlineKeyboardMarkupBuilder {
    private val buttons = mutableListOf<InlineKeyboardButton?>()

    fun url(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, url = value()))
        return this
    }

    fun callbackData(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, callbackData = value()))
        return this
    }

    fun webAppInfo(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, webApp = WebAppInfo(value())))
        return this
    }

    fun loginUrl(name: String, value: () -> com.github.vendelieu.tgbot.types.LoginUrl): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, loginUrl = value()))
        return this
    }

    fun switchInlineQuery(name: String, value: () -> String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, switchInlineQuery = value()))
        return this
    }

    fun callbackGame(name: String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, callbackGame = com.github.vendelieu.tgbot.types.CallbackGame))
        return this
    }

    fun pay(name: String): InlineKeyboardMarkupBuilder {
        buttons.add(InlineKeyboardButton(name, pay = true))
        return this
    }

    fun newLine() {
        buttons.add(null)
    }

    fun br() = newLine()

    infix fun String.callback(value: String) = callbackData(this) { value }
    infix fun String.url(value: String) = url(this) { value }
    infix fun String.webAppInfo(value: String) = webAppInfo(this) { value }
    infix fun String.loginUrl(value: com.github.vendelieu.tgbot.types.LoginUrl) = loginUrl(this) { value }
    infix fun String.switchInlineQuery(value: String) = switchInlineQuery(this) { value }
    infix fun String.callbackGame(value: Unit) = callbackGame(this)
    infix fun String.pay(value: Unit) = pay(this)

    infix fun InlineKeyboardMarkupBuilder.br(other: String): String {
        newLine()
        return other
    }

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

fun inlineKeyboardMarkup(
    block: InlineKeyboardMarkupBuilder.() -> Unit,
) = InlineKeyboardMarkup(*InlineKeyboardMarkupBuilder().apply(block).build().toTypedArray())
