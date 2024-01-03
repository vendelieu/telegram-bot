@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.core.TgUpdateHandler.Companion.logger
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Creates new coroutine context from parent one and adds supervisor job.
 *
 * @param parentContext Context that will be merged with the created one.
 */
@Suppress("NOTHING_TO_INLINE")
internal inline fun newCoroutineCtx(parentContext: CoroutineContext) = object : CoroutineScope {
    override val coroutineContext = parentContext + SupervisorJob(parentContext[Job]) + CoroutineName("TgBot")
}

internal suspend inline fun TgUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long? = null,
    actionId: String? = null,
): Boolean = bot.config.rateLimiter.run {
    if (limits.period == 0L && limits.rate == 0L || telegramId == null) return false

    logger.debug { "Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}." }
    if (mechanism.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info { "User #$telegramId has exceeded the request limit$loggingTail." }
        exceededAction.invoke(telegramId, bot)
        return true
    }
    return false
}

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> TgAction<List<Type>>.getInnerType(): Class<Type> =
    Type::class.java

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type> TgAction<Type>.getReturnType(): Class<Type> = Type::class.java

internal var mu.KLogger.level: Level
    get() = (underlyingLogger as Logger).level
    set(value) {
        (underlyingLogger as Logger).level = value
    }

internal val DEFAULT_COMMAND_SCOPE = setOf(UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY)
internal val PARAMETERS_MAP_TYPEREF = jacksonTypeRef<Map<String, Any?>>()
internal val RESPONSE_UPDATES_LIST_TYPEREF = jacksonTypeRef<Response<List<Update>>>()

internal suspend inline fun <T> asyncAction(crossinline block: suspend () -> T): Deferred<T> = coroutineScope {
    async { block() }
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun String.asClass(): Class<*>? = kotlin.runCatching { Class.forName(this) }.getOrNull()

@Suppress("NOTHING_TO_INLINE")
internal inline fun Class<*>?.getActions(postFix: String? = null) =
    this?.getMethod("get\$ACTIONS".let { if (postFix != null) it + postFix else it })?.invoke(null) as? List<*>

fun <T : ChainLink> InputListener.setChain(user: User, firstLink: T) = set(user, firstLink::class.qualifiedName!!)
