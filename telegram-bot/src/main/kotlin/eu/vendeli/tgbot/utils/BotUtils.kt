@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.StructuredRequest
import eu.vendeli.tgbot.types.internal.UpdateType
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

@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
internal fun TelegramUpdateHandler.parseCommand(
    text: String,
): StructuredRequest = with(bot.config.commandParsing) {
    var command = ""
    var commandFound = false
    val params = mutableMapOf<String, String>()

    var isParamNameFound = false
    var paramNameBuffer = ""
    var paramValBuffer = ""

    text.forEach { i ->
        when {
            i == commandDelimiter -> {
                commandFound = true
            }

            !commandFound && commandDelimiter != ' ' && restrictSpacesInCommands && i == ' ' -> {
                commandFound = true
            }

            !commandFound -> {
                command += i
            }

            i == parametersDelimiter -> {
                if (paramValBuffer.isEmpty()) {
                    params["param_${params.keys.indices.last + 2}"] = paramNameBuffer
                } else {
                    params[paramNameBuffer] = paramValBuffer
                }
                paramNameBuffer = ""
                paramValBuffer = ""

                isParamNameFound = false
            }

            i == parameterValueDelimiter -> {
                isParamNameFound = true
            }

            !isParamNameFound -> {
                paramNameBuffer += i
            }

            else -> {
                paramValBuffer += i
            }
        }
    }
    if (paramNameBuffer.isNotEmpty() && paramValBuffer.isNotEmpty()) {
        params[paramNameBuffer] = paramValBuffer
    }
    if (paramNameBuffer.isNotEmpty() && paramValBuffer.isEmpty()) {
        params["param_${params.keys.indices.last + 2}"] = paramNameBuffer
    }

    return StructuredRequest(command = command, params = params)
}

internal suspend inline fun Method.invokeSuspend(obj: Any, vararg args: Any?): Any? =
    suspendCoroutineUninterceptedOrReturn { cont ->
        invoke(obj, *args, cont)
    }

internal suspend inline fun TelegramUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long?,
    actionId: String? = null,
): Boolean {
    if (limits.period == 0L && limits.rate == 0L || telegramId == null) return false

    logger.trace { "Checking the request for exceeding the limits${if (actionId != null) " for $actionId}" else ""}." }
    if (rateLimiter.isLimited(limits, telegramId, actionId)) {
        val loggingTail = if (actionId != null) " for $actionId}" else ""
        logger.info { "User #$telegramId has exceeded the request limit$loggingTail." }
        rateLimiter.exceededLimitResponse(telegramId, bot)
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
internal fun TelegramUpdateHandler.findAction(
    text: String,
    command: Boolean = true,
    updateType: UpdateType,
): Activity? {
    val message = parseCommand(text)
    val invocation = if (command) actions?.commands else {
        actions?.inputs
    }?.get(message.command)
    if (invocation != null && command && updateType.scope !in invocation.scope)
        return null

    return if (invocation != null) Activity(
        id = message.command,
        invocation = invocation,
        parameters = message.params,
        rateLimits = invocation.rateLimits,
    ) else null
}

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> SimpleAction<List<Type>>.getInnerType(): Class<Type> =
    Type::class.java

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : MultipleResponse> Action<List<Type>>.getInnerType(): Class<Type> =
    Type::class.java

@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type> TgAction<Type>.getReturnType(): Class<Type> = Type::class.java

internal var mu.KLogger.level: Level
    get() = (underlyingLogger as Logger).level
    set(value) {
        (underlyingLogger as Logger).level = value
    }
