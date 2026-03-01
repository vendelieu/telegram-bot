package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ktnip.annotation.AnnotationExtractor
import eu.vendeli.ktnip.annotation.AnnotationParser
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.utils.expandToBaseAnnotations
import eu.vendeli.ktnip.utils.getAnnotatedFnSymbols
import eu.vendeli.tgbot.annotations.CommonHandler

/**
 * Collects CommonHandler.Text and CommonHandler.Regex annotated functions.
 * Generates Activities and registers them as matchers in the registry.
 */
internal class CommonCollector : BaseCollector() {
    override fun collect(resolver: Resolver, ctx: CollectorsContext) {
        ctx.logger.info("Collecting common handlers.")
        val targetNames = setOf(
            CommonHandler.Text::class.qualifiedName!!,
            CommonHandler.Regex::class.qualifiedName!!,
        )

        // Build registry of common handlers
        val registryBuilder = CommonHandlerRegistry.builder()

        resolver
            .getAnnotatedFnSymbols(
                ctx.pkg,
                CommonHandler.Text::class,
                CommonHandler.Regex::class,
            ).forEach { function ->
                function.annotations
                    .flatMap { it.expandToBaseAnnotations(targetNames) }
                    .forEach { baseAnno ->
                        // Parse annotation
                        val parsedData = AnnotationParser.parseCommonHandler(baseAnno.arguments)

                        // Extract metadata
                        val rateLimits = AnnotationExtractor.extractRateLimits(function)
                        val argParser = AnnotationExtractor.extractArgParser(function)

                        // Register in builder
                        registryBuilder.register(function, parsedData, rateLimits, argParser)
                    }
            }

        // Build immutable registry and process
        val commonHandlerRegistry = registryBuilder.build()

        commonHandlerRegistry.getAll().forEach { data ->
            val function = data.funDeclaration

            // Extract metadata
            val metadata = extractActivityMetadata(function)

            // Generate and register activity
            val activityId = generateAndRegisterActivity(
                function = function,
                metadata = metadata,
                ctx = ctx,
            )

            // Register matchers
            data.scope.forEach { updT ->
                ctx.logger.info(
                    "Common: ${
                        data.value.toCommonMatcher(
                            data.filters,
                            data.scope,
                        )
                    } UpdateType: ${updT.name} --> ${function.qualifiedName?.asString()}",
                )
                ctx.loadFun.addStatement(
                    "registerCommonHandler(%L, UpdateType.%L, %L.id)",
                    data.value.toCommonMatcher(data.filters, data.scope),
                    updT.name,
                    activityId,
                )
            }
        }
    }
}
