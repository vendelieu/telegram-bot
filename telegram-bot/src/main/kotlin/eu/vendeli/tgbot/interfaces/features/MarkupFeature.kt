package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.Keyboard
import eu.vendeli.tgbot.interfaces.ParametersBase

/**
 * Markup feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action itself.
 */
interface MarkupFeature<Return : MarkupAble> : ParametersBase {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * Add Markup directly
     *
     * @param keyboard
     * @return Action itself.
     */
    fun markup(keyboard: Keyboard): Return {
        parameters["reply_markup"] = keyboard
        return thisAsReturn
    }

    /**
     * Add Markup via lambda
     *
     * @param block
     * @return action itself.
     */
    fun markup(block: () -> Keyboard): Return = markup(block())
}
