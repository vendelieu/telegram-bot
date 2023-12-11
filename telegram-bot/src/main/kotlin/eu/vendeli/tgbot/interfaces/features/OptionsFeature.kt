package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.options.Options
import eu.vendeli.tgbot.utils.PARAMETERS_MAP_TYPEREF

/**
 * Options feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Action Action itself.
 * @param Opts Options Class
 */
interface OptionsFeature<Action : TgAction<*>, out Opts> : Feature where Opts : Options {
    /**
     * Lambda function to change options
     */
    @Suppress("UNCHECKED_CAST")
    fun options(block: Opts.() -> Unit): Action = (this as Action).apply {
        options?.also {
            block.invoke(it as Opts)
            parameters.putAll(mapper.convertValue(it, PARAMETERS_MAP_TYPEREF))
        }
    }
}
