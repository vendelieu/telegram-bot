package eu.vendeli.tgbot.interfaces.features

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.IActionState
import eu.vendeli.tgbot.types.internal.options.Options

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
    var options: Opts

    /**
     * Lambda function to change options
     */
    fun options(block: Opts.() -> Unit): Return {
        options = options.apply(block)
        parameters.putAll(mapper.convertValue(options, jacksonTypeRef<Map<String, Any?>>()))
        return thisAsReturn
    }
}
