package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.types.component.ProcessingContext

fun interface PipelineInterceptor {
    suspend operator fun invoke(context: ProcessingContext)
}
