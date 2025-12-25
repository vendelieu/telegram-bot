package eu.vendeli.ksp.collectors

import eu.vendeli.ksp.annotation.AnnotationExtractor
import eu.vendeli.ksp.codegen.ActivityCodeGenerator
import eu.vendeli.ksp.codegen.toActivityMetadata
import eu.vendeli.ksp.dto.ActivityMetadata
import eu.vendeli.ksp.dto.CollectorsContext
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.ksp.dto.LambdaParameters
import eu.vendeli.tgbot.types.component.UpdateType

/**
 * Base class for collectors providing common functionality.
 * Reduces duplication across collector implementations.
 */
abstract class BaseCollector : Collector {
    /**
     * Extracts activity metadata from a function with annotation extraction.
     *
     * @param function The function declaration
     * @return ActivityMetadata with extracted annotations
     */
    protected fun extractActivityMetadata(function: KSFunctionDeclaration): ActivityMetadata {
        val rateLimits = AnnotationExtractor.extractRateLimits(function)
        val guardClass = AnnotationExtractor.extractGuard(function)
        val argParserClass = AnnotationExtractor.extractArgParser(function)

        return function.toActivityMetadata(rateLimits, guardClass, argParserClass)
    }

    /**
     * Generates an Activity object and registers it in the context.
     *
     * @param function The function declaration
     * @param metadata Activity metadata
     * @param ctx Collectors context
     * @param parameters Additional lambda parameters
     * @param updateType Update type for special handling
     * @return The activity ID
     */
    protected fun generateAndRegisterActivity(
        function: KSFunctionDeclaration,
        metadata: ActivityMetadata,
        ctx: CollectorsContext,
        parameters: List<LambdaParameters> = emptyList(),
        updateType: UpdateType? = null,
    ): String {
        val activityCodeGenerator = ActivityCodeGenerator(ctx.activitiesFile, ctx.injectableTypes)
        val activityCodeBlock = activityCodeGenerator.buildActivityCodeBlock(
            function = function,
            metadata = metadata,
            ctx = ctx,
            parameters = parameters,
            updateType = updateType,
        )

        ctx.loadFun.addStatement("registerActivity(%L)", activityCodeBlock)
        return activityCodeBlock.toString()
    }
}
