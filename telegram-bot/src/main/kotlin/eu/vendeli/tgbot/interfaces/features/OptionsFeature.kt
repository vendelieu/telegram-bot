package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.types.internal.options.Options

/**
 * Options feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action itself.
 * @param Opts Options Class
 */
interface OptionsFeature<Return : OptionAble, Opts : Options> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
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
        parameters.putAll(options.getParams())
        return thisAsReturn
    }
}
