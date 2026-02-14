package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.interfaces.marker.InputSelfManaging
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.utils.common.parseCommand

internal object DefaultMatchInterceptor : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        val user = context.update.userOrNull
        context.activity = context.registry.findCommand(context.parsedInput, context)
        if (user != null) {
            val input = context.bot.inputListener
                .getAsync(user.id)
                .await()
            if (input != null) {
                if (context.activity == null) {
                    val request = context.bot.update.parseCommand(input)
                    context.parsedInput = request.command
                    context.activity = context.registry.findInput(context.parsedInput)
                }
                if (context.bot.config.inputAutoRemoval &&
                    context.activity !is InputSelfManaging
                ) context.bot.inputListener.del(user.id)
            }
        }

        if (context.activity == null) {
            context.activity = context.registry.findCommonHandler(context.parsedInput, context)
        }
    }
}
