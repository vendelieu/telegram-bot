package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.utils.internal.getParameters
import eu.vendeli.tgbot.utils.common.parseCommand

internal object DefaultParsingInterceptor : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        val request = context.bot.update.parseCommand(context.update.text)
        context.parsedInput = request.command
        context.parameters = context.bot.update.getParameters(context.activity?.argParser, request)
    }
}
