package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.Filter
import eu.vendeli.tgbot.utils.checkIsFiltered
import kotlin.reflect.KClass

sealed class CommonMatcher(
    open val value: Any,
    val filter: KClass<out Filter>,
) {
    abstract suspend fun match(text: kotlin.String, update: ProcessedUpdate, bot: TelegramBot): Boolean

    class String(
        override val value: kotlin.String,
        filter: KClass<out Filter>,
    ) : CommonMatcher(value, filter) {
        override suspend fun match(text: kotlin.String, update: ProcessedUpdate, bot: TelegramBot): Boolean =
            filter.checkIsFiltered(update.getUser(), update, bot) && text == value
    }

    class Regex(
        override val value: kotlin.text.Regex,
        filter: KClass<out Filter>,
    ) : CommonMatcher(value, filter) {
        override suspend fun match(text: kotlin.String, update: ProcessedUpdate, bot: TelegramBot): Boolean =
            filter.checkIsFiltered(update.getUser(), update, bot) && value.matchEntire(text) != null
    }
}
