package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ktnip.annotation.AnnotationParser
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.dto.CommandHandlerParams
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.ktnip.utils.getAnnotatedFnSymbols
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery
import eu.vendeli.tgbot.types.component.UpdateType

/**
 * Collects CommandHandler and CallbackQuery annotated functions.
 * Generates Activities and registers them as commands in the registry.
 */
internal class CommandCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting commands.")
        val symbols = resolver.getAnnotatedFnSymbols(ctx.pkg, CommandHandler::class, CallbackQuery::class)

        symbols.forEach { function ->
            // Determine annotation type
            var isCallbackQAnnotation = false
            val targetAnnotation = function.annotations.let {
                var anno = it.findAnnotationRecursively(CallbackQuery::class)
                if (anno != null) {
                    isCallbackQAnnotation = true
                } else {
                    anno = it.findAnnotationRecursively(CommandHandler::class)
                }
                anno ?: throw IllegalStateException("No CommandHandler annotation found for $function")
            }

            // Parse annotation
            val annotationData = AnnotationParser.parseCommandHandler(
                targetAnnotation.arguments,
                isCallbackQAnnotation,
            )

            // Extract metadata
            val metadata = extractActivityMetadata(function)

            // Determine parameters
            val params = if (
                annotationData.autoAnswer == true ||
                (ctx.autoAnswerCallback && annotationData.autoAnswer != false)
            ) {
                listOf(CommandHandlerParams.CallbackQueryAutoAnswer)
            } else {
                emptyList()
            }

            // Determine update type for code generation
            val updateType = if (isCallbackQAnnotation) UpdateType.CALLBACK_QUERY else null

            // Generate and register activity
            val activityId = generateAndRegisterActivity(
                function = function,
                metadata = metadata,
                ctx = ctx,
                parameters = params,
                updateType = updateType,
            )

            // Register commands
            annotationData.commands.forEach { command ->
                annotationData.scope.forEach { updT ->
                    ctx.logger.info("Command: $command UpdateType: ${updT.name} --> ${function.qualifiedName?.asString()}")
                    ctx.loadFun.addStatement(
                        "registerCommand(%S, UpdateType.%L, %L.id)",
                        command,
                        updT.name,
                        activityId,
                    )
                }
            }
        }
    }
}
