package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.utils.internal.ClassCrumbsStore

internal object DefaultPostInvokeInterceptor : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        val utils = context.bot.update.____ctxUtils
            ?: error("Context is not initialized properly") // should never happen
        if (!utils.isClassDataInitialized.isInitialized()) return // user is not using classData

        val user = context.update.userOrNull ?: return // there's no user in this update
        val activity = context.activity ?: return // update is not handled by any activity so there's no crumbs


        ClassCrumbsStore.set(user.id, activity.qualifier)
    }
}
