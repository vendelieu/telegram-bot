package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.utils.common.checkIsLimited

internal object DefaultSetupInterceptor : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        if (context.bot.update.checkIsLimited(context.bot.config.rateLimiter.limits, context.update.userOrNull?.id)) {
            context.finish()
        }
    }
}
