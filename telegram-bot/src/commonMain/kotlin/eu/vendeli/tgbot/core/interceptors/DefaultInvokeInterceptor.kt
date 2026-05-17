package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.Activity
import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.utils.common.handleFailure
import io.ktor.util.logging.*

internal object DefaultInvokeInterceptor : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        val logger = context.bot.config.loggerFactory
            .get("eu.vendeli.core.interceptors.InvokeInterceptor")
        context.registry.getUpdateTypeHandlers(context.update).forEach {
            it.invokeCatching(context, logger)
        }

        val activity = context.activity ?: context.registry.getUnprocessedHandler()
        if (activity == null) {
            logger.warn("update: ${context.update.toJsonString()} not handled.")
            return
        }

        activity.invokeCatching(context, logger)
    }

    private suspend fun Activity.invokeCatching(
        context: ProcessingContext,
        logger: Logger,
    ) = runCatching { invoke(context) }
        .onFailure {
            logger.error(
                "Invocation e" +
                    "rror while handling update in ${this::class}[${context.parsedInput}] " +
                    "with update: ${context.update.toJsonString()}",
                it,
            )
            context.bot.update.handleFailure(context.update, it)
        }.onSuccess {
            logger.debug {
                "Handled update#${context.update.updateId} to $qualifier::$function [${this::class.simpleName}]"
            }
        }
}
