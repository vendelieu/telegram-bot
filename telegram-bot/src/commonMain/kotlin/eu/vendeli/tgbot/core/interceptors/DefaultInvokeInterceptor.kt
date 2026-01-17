package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.Activity
import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.utils.common.handleFailure
import io.ktor.util.logging.*

internal object DefaultInvokeInterceptor : PipelineInterceptor {
    private val userClassCrumbs = mutableMapOf<Long, String>()

    override suspend fun invoke(context: ProcessingContext) {
        val logger = context.bot.config.loggerFactory
            .get("eu.vendeli.core.interceptors.InvokeInterceptor")
        context.registry.getUpdateTypeHandlers(context.update.type).forEach {
            it.invokeCatching(context, logger)
        }

        val activity = context.activity ?: context.registry.getUnprocessedHandler()
        if (activity == null) {
            logger.warn("update: ${context.update.toJsonString()} not handled.")
            return
        }

        handleClassCrumbs(context = context, clean = true)
        activity.invokeCatching(context, logger)
        handleClassCrumbs(context)
    }

    private suspend fun Activity.invokeCatching(
        context: ProcessingContext,
        logger: Logger,
    ) = runCatching { invoke(context) }
        .onFailure {
            logger.error(
                "Invocation error while handling update in ${this::class}[${context.parsedInput}] " +
                    "with update: ${context.update.toJsonString()}",
                it,
            )
            context.bot.update.handleFailure(context.update, it)
        }.onSuccess {
            logger.debug {
                "Handled update#${context.update.updateId} to $qualifier::$function [${this::class.simpleName}]"
            }
        }

    private suspend fun handleClassCrumbs(context: ProcessingContext, clean: Boolean = false) {
        val user = context.update.userOrNull ?: return
        if (
            context.activity == null ||
            context.bot.update.____ctxUtils
                ?.isClassDataInitialized
                ?.isInitialized() == false
        ) return

        if (
            clean &&
            context.activity!!.qualifier != userClassCrumbs[user.id]
        ) {
            context.bot.update.____ctxUtils
                ?.clearClassData(user.id)
            return
        }
        userClassCrumbs[user.id] = context.activity!!.qualifier
    }
}
