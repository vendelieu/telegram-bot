package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.utils.common.checkIsLimited
import eu.vendeli.tgbot.utils.internal.checkIsGuarded
import io.ktor.util.logging.*

internal object DefaultValidationInterceptor : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        val logger = context.bot.config.loggerFactory
            .get("eu.vendeli.core.interceptors.ValidationInterceptor")
        val activity = context.activity ?: return
        val user = context.update.userOrNull

        val isGuarded = activity.guardClass.checkIsGuarded(user, context.update, context.bot)
        if (!isGuarded) {
            logger.debug { "Invocation guarded: $activity" }
            context.finish()
            return
        }

        if (context.bot.update.checkIsLimited(activity.rateLimits, user?.id, activity.id.toString())) {
            context.finish()
        }
    }
}
