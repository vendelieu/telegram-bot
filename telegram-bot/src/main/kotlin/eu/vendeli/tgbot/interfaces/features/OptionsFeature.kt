package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.types.internal.options.Options

interface OptionsFeature<Return : OptionAble, Opts : Options> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    var options: Opts

    fun options(block: Opts.() -> Unit): Return {
        options = options.also(block)
        parameters.putAll(options.getParams())
        return thisAsReturn
    }
}
