package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ktnip.annotation.AnnotationParser
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.ktnip.utils.getAnnotatedFnSymbols
import eu.vendeli.tgbot.annotations.UpdateHandler

/**
 * Collects UpdateHandler annotated functions.
 * Generates Activities and registers them as update type handlers in the registry.
 */
internal class UpdateHandlerCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting update handlers.")
        val symbols = resolver.getAnnotatedFnSymbols(ctx.pkg, UpdateHandler::class)

        symbols.forEach { function ->
            // Parse annotation
            val annotation = function.annotations
                .findAnnotationRecursively(UpdateHandler::class)
                ?: throw IllegalStateException("No UpdateHandler annotation found for $function")

            val updateTypes = AnnotationParser.parseUpdateHandler(annotation.arguments)

            // Extract metadata
            val metadata = extractActivityMetadata(function)

            // Generate and register activity
            val activityId = generateAndRegisterActivity(
                function = function,
                metadata = metadata,
                ctx = ctx,
            )

            // Register update type handlers
            updateTypes.forEach { updT ->
                ctx.logger.info("UpdateHandler: ${updT.name} --> ${function.qualifiedName?.asString()}")
                ctx.loadFun.addStatement(
                    "registerUpdateTypeHandler(UpdateType.%L, %L.id)",
                    updT.name,
                    activityId,
                )
            }
        }
    }
}
