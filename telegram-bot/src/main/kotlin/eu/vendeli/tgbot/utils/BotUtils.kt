@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.types.internal.StructuredRequest
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.lang.reflect.Method
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * Creates new coroutine context from parent one and adds supervisor job.
 *
 * @param parentContext Context that will be merged with the created one.
 */
internal class NewCoroutineContext(parentContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        parentContext + SupervisorJob(parentContext[Job]) + CoroutineName("TgBot")
}

internal fun String.parseQuery(): StructuredRequest {
    val reqParams = split('?')

    return StructuredRequest(
        command = reqParams.first(),
        params = reqParams.last().split('&').associate { it.substringBefore('=') to it.substringAfter('=') },
    )
}

internal fun String.parseKeyValueBySpace(): StructuredRequest {
    val parsedString = split(' ')

    return StructuredRequest(
        parsedString.first(),
        parsedString.chunked(2).associate { it.first() to (it.lastOrNull() ?: "") },
    )
}

internal suspend fun Method.invokeSuspend(obj: Any, vararg args: Any?): Any? =
    suspendCoroutineUninterceptedOrReturn { cont ->
        invoke(obj, *args, cont)
    }

internal suspend fun TelegramUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long?,
    actionId: String? = null,
): Boolean {
    if (limits.period == 0L && limits.rate == 0L || telegramId == null) return false

    logger.trace("Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}.")
    if (rateLimiter.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info("User #$telegramId has exceeded the request limit$loggingTail.")
        rateLimiter.exceededLimitResponse(telegramId, bot)
        return true
    }
    return false
}
