package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.types.internal.TgMethod

/**
 * Tg action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 */
interface TgAction {
    /**
     * A method that is implemented in Action.
     */
    val method: TgMethod

    /**
     * Parameter through which the type for multiple response is obtained.
     */
    val wrappedDataType get(): Class<out MultipleResponse>? = null
}
