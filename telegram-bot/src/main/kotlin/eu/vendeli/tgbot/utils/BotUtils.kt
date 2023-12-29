@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.core.ReflectionUpdateHandler
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.core.TgUpdateHandler.Companion.logger
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.StructuredRequest
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.reflect.Method
import java.lang.reflect.Modifier.isStatic
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * Creates new coroutine context from parent one and adds supervisor job.
 *
 * @param parentContext Context that will be merged with the created one.
 */
@Suppress("NOTHING_TO_INLINE")
internal inline fun newCoroutineCtx(parentContext: CoroutineContext) = object : CoroutineScope {
    override val coroutineContext = parentContext + SupervisorJob(parentContext[Job]) + CoroutineName("TgBot")
}

@Suppress("SpreadOperator")
internal suspend inline fun Method.handleInvocation(
    clazz: Class<*>,
    classManager: ClassManager,
    parameters: Array<Any?>,
    isSuspend: Boolean = false,
): Any? {
    val obj = when {
        isStatic(modifiers) -> null
        else -> classManager.getInstance(clazz)
    }

    return if (isSuspend) suspendCoroutineUninterceptedOrReturn { cont ->
        invoke(obj, *parameters, cont)
    } else invoke(obj, *parameters)
}

internal suspend inline fun TgUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long?,
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

/**
 * Function for mapping text with a specific command or input.
 *
 * @param text
 * @param command true to search in commands or false to search among inputs. Default - true.
 * @return [Activity] if actions was found or null.
 */
internal fun ReflectionUpdateHandler.findAction(
    text: String,
    command: Boolean = true,
    updateType: UpdateType,
): Activity? {
    val message = parseCommand(text)
    val invocation = if (command) {
        actions?.commands
    } else {
        actions?.inputs
    }?.get(message.command)

    if (invocation != null && command && updateType !in invocation.scope)
        return null

    if (command && invocation == null) actions?.regexCommands?.entries?.firstOrNull {
        it.key.matchEntire(text) != null
    }?.also {
        return it.value.toActivity(message)
    }

    return invocation?.toActivity(message)
}

internal fun Invocation.toActivity(req: StructuredRequest) = Activity(
    id = req.command,
    invocation = this,
    parameters = req.params,
    rateLimits = rateLimits,
)

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
