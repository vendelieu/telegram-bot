package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.Options
import kotlin.properties.Delegates

/**
 * Tg action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 */
abstract class TgAction<ReturnType> {
    /**
     * A method that is implemented in Action.
     */
    internal open val method by Delegates.notNull<TgMethod>()

    /**
     * The parameter that stores the options.
     */
    internal open val options: Options? = null

    /**
     * Type of action result.
     */
    internal open val returnType by Delegates.notNull<Class<ReturnType>>()

    /**
     * Parameter through which the type for multiple response is obtained.
     */
    internal open val wrappedDataType: Class<out MultipleResponse>? = null

    /**
     * Action data storage parameter.
     */
    internal val parameters: MutableMap<String, Any?> = mutableMapOf()
}
