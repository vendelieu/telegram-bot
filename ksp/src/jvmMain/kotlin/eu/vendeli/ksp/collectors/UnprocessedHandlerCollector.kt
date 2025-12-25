package eu.vendeli.ksp.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.utils.getAnnotatedFnSymbols
import eu.vendeli.tgbot.annotations.UnprocessedHandler

/**
 * Collects UnprocessedHandler annotated function (fallback handler).
 * Generates Activity and registers it as the unprocessed handler in the registry.
 * Note: Only one unprocessed handler is supported per project.
 */
internal class UnprocessedHandlerCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting unprocessed handler.")
        val function = resolver.getAnnotatedFnSymbols(ctx.pkg, UnprocessedHandler::class).firstOrNull() ?: return

        // Extract metadata
        val metadata = extractActivityMetadata(function)

        // Generate and register activity
        val activityId = generateAndRegisterActivity(
            function = function,
            metadata = metadata,
            ctx = ctx,
        )

        ctx.logger.info("UnprocessedHandler --> ${function.qualifiedName?.asString()}")
        ctx.loadFun.addStatement("registerUnprocessed(%L.id)", activityId)
    }
}
