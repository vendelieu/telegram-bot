package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.core.interceptors.*
import eu.vendeli.tgbot.types.component.ProcessingContext

class ProcessingPipeline internal constructor() {
    private val interceptors = mutableMapOf<ProcessingPipePhase, MutableList<PipelineInterceptor>>()
    private val phases = listOf(
        ProcessingPipePhase.Setup,
        ProcessingPipePhase.Parsing,
        ProcessingPipePhase.Match,
        ProcessingPipePhase.Validation,
        ProcessingPipePhase.PreInvoke,
        ProcessingPipePhase.Invoke,
        ProcessingPipePhase.PostInvoke,
    )

    init {
        intercept(ProcessingPipePhase.Setup, DefaultSetupInterceptor)
        intercept(ProcessingPipePhase.Parsing, DefaultParsingInterceptor)
        intercept(ProcessingPipePhase.Match, DefaultMatchInterceptor)
        intercept(ProcessingPipePhase.Validation, DefaultValidationInterceptor)
        intercept(ProcessingPipePhase.Invoke, DefaultInvokeInterceptor)
    }

    fun intercept(phase: ProcessingPipePhase, interceptor: PipelineInterceptor) {
        interceptors.getOrPut(phase) { mutableListOf() }.add(interceptor)
    }

    suspend fun execute(context: ProcessingContext) {
        for (phase in phases) {
            if (!context.shouldProceed) break
            interceptors[phase]?.forEach { interceptor ->
                if (!context.shouldProceed) return@forEach
                interceptor(context)
            }
        }
    }
}
