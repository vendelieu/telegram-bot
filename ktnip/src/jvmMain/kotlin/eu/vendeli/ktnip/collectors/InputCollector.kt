package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ktnip.annotation.AnnotationParser
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.checkForInapplicableAnnotations
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.ktnip.utils.getAnnotatedFnSymbols
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.InputHandler

/**
 * Collects InputHandler annotated functions.
 * Generates Activities and registers them as input handlers in the registry.
 */
internal class InputCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting inputs.")
        val inputHandlerSymbols = resolver.getAnnotatedFnSymbols(ctx.pkg, InputHandler::class)

        inputHandlerSymbols.forEach { function ->
            // Parse annotation
            val annotation = function.annotations
                .findAnnotationRecursively(InputHandler::class)
                ?: throw IllegalStateException("No InputHandler annotation found for $function")

            val inputIds = AnnotationParser.parseInputHandler(annotation.arguments)

            // Extract metadata
            val metadata = extractActivityMetadata(function)

            // Check for inapplicable annotations
            function.checkForInapplicableAnnotations(
                InputHandler::class.simpleName!!,
                ctx.logger,
                ArgParser::class.simpleName!!,
            )

            // Generate and register activity
            val activityId = generateAndRegisterActivity(
                function = function,
                metadata = metadata,
                ctx = ctx,
            )

            // Register input IDs
            inputIds.forEach { inputId ->
                ctx.logger.info("Input: $inputId --> ${function.qualifiedName?.asString()}")
                ctx.loadFun.addStatement("registerInput(%S, %L.id)", inputId, activityId)
            }
        }
    }
}
