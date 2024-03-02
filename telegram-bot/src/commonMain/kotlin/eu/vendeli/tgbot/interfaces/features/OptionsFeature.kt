package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.options.Options
import eu.vendeli.tgbot.utils.serde
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import kotlinx.serialization.json.jsonObject

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
        (options as? Opts)?.also {
            block.invoke(it)
            parameters.putAll(serde.encodeToJsonElement(DynamicLookupSerializer, it).jsonObject)
        }
    }
}
