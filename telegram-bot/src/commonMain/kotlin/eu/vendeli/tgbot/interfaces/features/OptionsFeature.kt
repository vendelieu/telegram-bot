package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.action.TgAction
import eu.vendeli.tgbot.types.internal.options.Options
import eu.vendeli.tgbot.utils.cast
import eu.vendeli.tgbot.utils.safeCast
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
    fun options(block: Opts.() -> Unit): Action = this.cast<Action>().apply {
        options.safeCast<Opts>()?.also {
            block.invoke(it)
            parameters.putAll(serde.encodeToJsonElement(DynamicLookupSerializer, it).jsonObject)
        }
    }
}
