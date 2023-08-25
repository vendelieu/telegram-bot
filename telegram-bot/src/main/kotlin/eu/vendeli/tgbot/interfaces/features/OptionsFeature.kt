package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.IActionState
import eu.vendeli.tgbot.types.internal.options.Options
import eu.vendeli.tgbot.utils.PARAMETERS_MAP_TYPEREF

/**
 * Options feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action itself.
 * @param Opts Options Class
 */
interface OptionsFeature<Return, Opts : Options> : IActionState, Feature {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * The parameter that stores the options.
     */
    val OptionsFeature<Return, Opts>.options: Opts

    /**
     * Lambda function to change options
     */
    fun options(block: Opts.() -> Unit): Return {
        parameters.putAll(mapper.convertValue(options.apply(block), PARAMETERS_MAP_TYPEREF))
        return thisAsReturn
    }
}
