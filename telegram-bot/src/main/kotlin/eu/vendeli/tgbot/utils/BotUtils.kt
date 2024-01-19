@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.core.TgUpdateHandler.Companion.logger
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal suspend inline fun TgUpdateHandler.coHandle(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline block: suspend CoroutineScope.() -> Unit,
) = (handlerScope + Job(handlerScope.coroutineContext[Job])).launch(dispatcher) { block() }

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

internal inline val <K, V : Any> Map<K, V>.logString: String
    get() = takeIf { isNotEmpty() }?.entries?.joinToString(",\n") {
        "${it.key} - " + if (it.value is Pair<*, *>) {
            (it.value as Pair<*, *>).second
        } else {
            it.value
        }.toString()
    } ?: "None"

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> TgAction<List<Type>>.getCollectionReturnType(): CollectionType =
    mapper.typeFactory.constructCollectionType(List::class.java, Type::class.java)

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type> TgAction<Type>.getReturnType(): JavaType =
    mapper.typeFactory.constructType(Type::class.java)

internal var mu.KLogger.level: Level
    get() = (underlyingLogger as Logger).level
    set(value) {
        (underlyingLogger as Logger).level = value
    }

internal val GET_UPDATES_ACTION = getUpdates()
internal val DEFAULT_COMMAND_SCOPE = setOf(UpdateType.MESSAGE)
internal val PARAMETERS_MAP_TYPEREF = jacksonTypeRef<Map<String, Any?>>()

internal suspend inline fun <T> asyncAction(crossinline block: suspend () -> T): Deferred<T> = coroutineScope {
    async { block() }
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun String.asClass(): Class<*>? = kotlin.runCatching { Class.forName(this) }.getOrNull()

@Suppress("NOTHING_TO_INLINE")
internal inline fun Class<*>?.getActions(postFix: String? = null) =
    this?.getMethod("get\$ACTIVITIES".let { if (postFix != null) it + postFix else it })?.invoke(null) as? List<*>

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
internal inline fun <T> Any?.cast() = this as T

fun <T : ChainLink> InputListener.setChain(user: User, firstLink: T) = set(user, firstLink::class.qualifiedName!!)
