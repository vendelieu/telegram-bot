package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.utils.checkIsFiltered
import kotlin.reflect.KClass

sealed class CommonMatcher(
    open val value: Any,
    val filter: KClass<out Filter>,
    val scope: Set<UpdateType>,
) {
    abstract suspend fun match(text: kotlin.String, update: ProcessedUpdate, bot: TelegramBot): Boolean

    class String(
        override val value: kotlin.String,
        filter: KClass<out Filter>,
        scope: Set<UpdateType>,
    ) : CommonMatcher(value, filter, scope) {
        override suspend fun match(text: kotlin.String, update: ProcessedUpdate, bot: TelegramBot): Boolean =
            update.type in scope &&
                filter.checkIsFiltered(update.userOrNull, update, bot) &&
                text == value
    }

    class Regex(
        override val value: kotlin.text.Regex,
        filter: KClass<out Filter>,
        scope: Set<UpdateType>,
    ) : CommonMatcher(value, filter, scope) {
        override suspend fun match(text: kotlin.String, update: ProcessedUpdate, bot: TelegramBot): Boolean =
            update.type in scope &&
                filter.checkIsFiltered(update.userOrNull, update, bot) &&
                value.matchEntire(text) != null
    }

    override fun toString(): kotlin.String =
        "${this::class.simpleName}(value = $value, filter = $filter, scope = $scope)"
}
