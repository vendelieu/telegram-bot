package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull

internal object DefaultInvokeInterceptor : PipelineInterceptor {
    private val userClassCrumbs = mutableMapOf<Long, String>()

    override suspend fun invoke(context: ProcessingContext) {
        val logger = context.bot.config.loggerFactory.get("eu.vendeli.core.interceptors.InvokeInterceptor")
        context.registry.getUpdateTypeHandlers(context.update.type).forEach {
            it.invoke(context)
        }

        val activity = context.activity ?: context.registry.getUnprocessedHandler()
        if (activity == null) {
            logger.warn("update: ${context.update.toJsonString()} not handled.")
            return
        }

        handleClassCrumbs(context = context, clean = true)
        activity.invoke(context)
        handleClassCrumbs(context)
    }

    private suspend fun handleClassCrumbs(context: ProcessingContext, clean: Boolean = false) {
        val user = context.update.userOrNull ?: return
        if (
            context.activity == null ||
            context.bot.update.____ctxUtils?.isClassDataInitialized?.isInitialized() == false
        ) return

        if (
            clean &&
            context.activity!!.qualifier != userClassCrumbs[user.id]
        ) {
            context.bot.update.____ctxUtils?.clearClassData(user.id)
            return
        }
        userClassCrumbs[user.id] = context.activity!!.qualifier
    }
}
