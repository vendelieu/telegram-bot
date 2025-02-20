@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.types.component.ExceptionHandlingStrategy
import eu.vendeli.tgbot.types.component.FailedUpdate
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.internal.debug
import eu.vendeli.tgbot.utils.internal.info
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.reflect.KClass

inline fun <reified T : Any> ClassManager.getInstance(vararg initParams: Any?): T? =
    getInstance(T::class, *initParams) as? T

@KtGramInternal
@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
expect val _OperatingActivities: Map<String, List<Any?>>

expect val KClass<*>.fqName: String

internal suspend inline fun TgUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long? = null,
    actionId: String? = null,
): Boolean = bot.config.rateLimiter.run {
    if (telegramId == null || limits.period == 0L && limits.rate == 0L) return false

    logger.debug { "Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}." }
    if (mechanism.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info { "User #$telegramId has exceeded the request limit$loggingTail." }
        exceededAction.invoke(telegramId, bot)
        return true
    }
    return false
}

internal suspend inline fun TgUpdateHandler.handleFailure(
    update: ProcessedUpdate,
    throwable: Throwable,
) = when (val strategy = bot.config.exceptionHandlingStrategy) {
    is ExceptionHandlingStrategy.CollectToChannel -> caughtExceptions.send(FailedUpdate(throwable, update))
    is ExceptionHandlingStrategy.DoNothing -> {}
    is ExceptionHandlingStrategy.Throw ->
        throw TgException(message = "Caught exception while processing $update", cause = throwable)

    is ExceptionHandlingStrategy.Handle -> strategy.handler.handle(throwable, update, bot)
}

internal suspend inline fun <T> asyncAction(crossinline block: suspend () -> T): Deferred<T> = coroutineScope {
    async { block() }
}

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
internal inline fun <T> Any?.cast() = this as T

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
internal inline fun <T> Any?.safeCast() = this as? T
