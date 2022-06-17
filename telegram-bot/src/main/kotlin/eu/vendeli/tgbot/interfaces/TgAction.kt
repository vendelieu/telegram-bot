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
     * Method through which the type for multiple response is obtained.
     *
     * @param T inner type of multiple response.
     */
    fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T>? = null
}
