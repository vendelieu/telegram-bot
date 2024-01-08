package eu.vendeli.tgbot.interfaces

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.CollectionType
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
    internal open val returnType: JavaType? = null

    /**
     * Parameter through which the type for multiple response is obtained.
     */
    internal open val collectionReturnType: CollectionType? = null

    /**
     * Action data storage parameter.
     */
    internal val parameters: MutableMap<String, Any?> = mutableMapOf()
}
