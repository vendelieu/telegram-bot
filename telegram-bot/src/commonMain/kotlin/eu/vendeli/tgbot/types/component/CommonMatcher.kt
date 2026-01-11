package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.utils.common.DEFAULT_SCOPE
import eu.vendeli.tgbot.utils.common.ProcessingCtxKey
import eu.vendeli.tgbot.utils.internal.checkIsNotFiltered
import kotlin.reflect.KClass

sealed class CommonMatcher(
    open val value: Any,
    val filters: Set<KClass<out Filter>> = DEFAULT_FILTERS,
    val scope: Set<UpdateType>,
) {
    abstract suspend fun match(
        text: kotlin.String,
        context: ProcessingContext,
    ): Boolean

    class String(
        override val value: kotlin.String,
        filters: Set<KClass<out Filter>> = DEFAULT_FILTERS,
        scope: Set<UpdateType> = DEFAULT_SCOPE,
    ) : CommonMatcher(value, filters, scope) {
        override suspend fun match(
            text: kotlin.String,
            context: ProcessingContext,
        ): Boolean = context.run {
            update.type in scope &&
                filters.all { it.checkIsNotFiltered(update.userOrNull, update, bot) } &&
                text == value
        }
    }

    class Regex(
        override val value: kotlin.text.Regex,
        filters: Set<KClass<out Filter>> = DEFAULT_FILTERS,
        scope: Set<UpdateType> = DEFAULT_SCOPE,
    ) : CommonMatcher(value, filters, scope) {
        override suspend fun match(
            text: kotlin.String,
            context: ProcessingContext,
        ): Boolean = context.run {
            update.type in scope &&
                filters.all { it.checkIsNotFiltered(context.update.userOrNull, update, bot) } &&
                value.matchEntire(text)?.also {
                    additionalContext.state[ProcessingCtxKey.RegexMatch] = it
                } != null
        }
    }

    internal companion object {
        val DEFAULT_FILTERS = emptySet<KClass<out Filter>>()
    }

    override fun toString(): kotlin.String =
        "${this::class.simpleName}(value = $value, filters = $filters, scope = $scope)"
}
