package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Actions
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.findAction
import eu.vendeli.tgbot.utils.handleInvocation
import eu.vendeli.tgbot.utils.processUpdate

/**
 * A class that handles updates via reflection.
 *
 * @property actions The list of actions the handler will work with.
 * @property bot An instance of [TelegramBot]
 */
@Suppress("unused")
class ReflectionUpdateHandler internal constructor(
    internal val actions: Actions? = null,
    bot: TelegramBot,
) : TgUpdateHandler(bot) {
    /**
     * Function used to call functions with certain parameters processed after receiving update.
     *
     * @param pUpdate
     * @param invocation
     * @param parameters
     */
    @Suppress("CyclomaticComplexMethod")
    private suspend fun invokeMethod(
        pUpdate: ProcessedUpdate,
        invocation: Invocation,
        parameters: Map<String, String>,
    ) {
        var isSuspend = false
        logger.debug { "Parsing arguments for Update#${pUpdate.updateId}" }
        val processedParameters = buildList {
            invocation.method.parameters.forEach { p ->
                if (p.type.name == "kotlin.coroutines.Continuation") {
                    isSuspend = true
                    return@forEach
                }
                val parameterName = invocation.namedParameters.getOrDefault(p.name, p.name)
                val typeName = p.parameterizedType.typeName
                if (parameterName in parameters.keys) when (p.parameterizedType.typeName) {
                    "java.lang.String" -> add(parameters[parameterName].toString())
                    "java.lang.Integer", "int" -> add(parameters[parameterName]?.toIntOrNull())
                    "java.lang.Long", "long" -> add(parameters[parameterName]?.toLongOrNull())
                    "java.lang.Short", "short" -> add(parameters[parameterName]?.toShortOrNull())
                    "java.lang.Float", "float" -> add(parameters[parameterName]?.toFloatOrNull())
                    "java.lang.Double", "double" -> add(parameters[parameterName]?.toDoubleOrNull())
                    else -> add(null)
                } else when {
                    typeName == User::class.java.canonicalName -> add(pUpdate.userOrNull)
                    typeName == TelegramBot::class.java.canonicalName -> add(bot)
                    typeName == ProcessedUpdate::class.java.canonicalName -> add(pUpdate)
                    typeName == MessageUpdate::class.java.canonicalName -> add(pUpdate)
                    typeName == CallbackQueryUpdate::class.java.canonicalName -> add(pUpdate)
                    p.type in bot.autowiringObjects -> add(bot.autowiringObjects[p.type]?.get(pUpdate, bot))
                    else -> add(null)
                }
            }
        }.also { logger.debug { "Parsed arguments - $it." } }.toTypedArray()

        bot.chatData.run {
            if (pUpdate.userOrNull == null) return@run
            // check for user id nullability
            val prevClassName = getAsync<String>(pUpdate.userOrNull!!.id, "PrevInvokedClass").await()
            if (prevClassName != invocation.clazz.name) clearAllAsync(pUpdate.userOrNull!!.id).await()

            setAsync(pUpdate.userOrNull!!.id, "PrevInvokedClass", invocation.clazz.name).await()
        }

        logger.debug { "Invoking function for Update#${pUpdate.updateId}" }
        invocation.runCatching {
            method.handleInvocation(clazz, bot.config.classManager, processedParameters, isSuspend)
        }.onFailure {
            logger.error(it) { "Method $invocation invocation error at handling update: $pUpdate" }
            caughtExceptions.send(FailedUpdate(it.cause ?: it, pUpdate.update))
        }.onSuccess {
            logger.info { "Handled update#${pUpdate.updateId} to ${invocation.type} method ${invocation.method}" }
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun String.getActivityOrNull(user: User? = null, updateType: UpdateType): Activity? {
        var activity = findAction(substringBefore('@'), updateType = updateType)

        if (user != null && activity == null) {
            activity = bot.inputListener.get(user.id)?.let {
                findAction(it, false, updateType)
            }
        }
        if (user != null) bot.inputListener.del(user.id)
        logger.debug { "Result of finding action - ${activity?.invocation?.type ?: ""} $activity" }

        return activity
    }

    override suspend fun handle(update: Update) = update.processUpdate().run {
        logger.debug { "Handling update: $update" }
        if (checkIsLimited(bot.config.rateLimiter.limits, userOrNull?.id)) return@run

        val action = text.getActivityOrNull(userOrNull, type)

        if (action != null && checkIsLimited(action.rateLimits, userOrNull?.id, action.id))
            return@run

        actions?.updateHandlers?.get(type)?.also { invokeMethod(this, it, emptyMap()) }

        when {
            action != null -> invokeMethod(this, action.invocation, action.parameters)

            actions?.unhandled != null -> invokeMethod(this, actions.unhandled, emptyMap())

            else -> logger.warn { "update: $update not handled." }
        }
    }
}
