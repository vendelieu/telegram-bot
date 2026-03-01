package eu.vendeli.ktnip.codegen

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import eu.vendeli.ktnip.dto.ActivityMetadata
import eu.vendeli.ktnip.dto.CollectorsContext
import eu.vendeli.ktnip.dto.LambdaParameters
import eu.vendeli.ktnip.utils.FileBuilder
import eu.vendeli.ktnip.utils.TypeConstants
import eu.vendeli.ktnip.utils.getActivityId
import eu.vendeli.ktnip.utils.getActivityObjectName
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.fqName

/**
 * Generates Activity objects from function declarations.
 * Orchestrates ParameterResolver and InvocationCodeGenerator.
 */
class ActivityCodeGenerator(
    private val fileBuilder: FileBuilder,
    injectableTypes: Map<TypeName, ClassName>,
) {
    private val parameterResolver = ParameterResolver(injectableTypes)
    private val invocationCodeGenerator = InvocationCodeGenerator(fileBuilder)

    /**
     * Builds an Activity object code block for a given function.
     *
     * @param function The function declaration
     * @param metadata Activity metadata
     * @param ctx Collectors context for options
     * @param parameters Additional lambda parameters
     * @param updateType Update type for special handling
     * @return CodeBlock referencing the generated Activity object
     */
    fun buildActivityCodeBlock(
        function: KSFunctionDeclaration,
        metadata: ActivityMetadata,
        ctx: CollectorsContext? = null,
        parameters: List<LambdaParameters> = emptyList(),
        updateType: UpdateType? = null,
    ): CodeBlock {
        val isTopLvl = function.functionKind == FunctionKind.TOP_LEVEL
        val funQualifier = function.qualifiedName!!.getQualifier()
        val funShortName = function.simpleName.getShortName()
        val funName = if (!isTopLvl) {
            "$funQualifier::$funShortName"
        } else {
            fileBuilder.addImport(funQualifier, funShortName)
            "::$funShortName"
        }

        val isObject = (function.parent as? KSClassDeclaration)?.classKind == ClassKind.OBJECT
        val objectName = function.getActivityObjectName()

        // Build Activity object
        val activityObject = TypeSpec
            .objectBuilder(objectName)
            .addSuperinterface(TypeConstants.activity)
            .addModifiers(KModifier.INTERNAL)
            .addActivityProperties(metadata)

        // Resolve parameters
        val parameterStrategies = parameterResolver.resolveAll(function.parameters)

        // Determine if instance is needed
        val hasInstance = !isTopLvl && !isObject && function.functionKind != FunctionKind.STATIC
        val instanceQualifier = if (hasInstance) funQualifier else null

        // Build invoke function
        val invokeFun = FunSpec
            .builder("invoke")
            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
            .addParameter("context", TypeConstants.processingCtx)
            .returns(ANY.copy(nullable = true))
            .addCode(
                invocationCodeGenerator.generateInvocationCode(
                    funName = funName,
                    hasInstance = hasInstance,
                    instanceQualifier = instanceQualifier,
                    parameterStrategies = parameterStrategies,
                    updateType = updateType,
                    parameters = parameters,
                ),
            )

        activityObject.addFunction(invokeFun.build())
        fileBuilder.addType(activityObject.build())

        return CodeBlock.of("%N", objectName)
    }

    /**
     * Adds Activity properties to the TypeSpec builder.
     */
    private fun TypeSpec.Builder.addActivityProperties(metadata: ActivityMetadata) = apply {
        addProperty(
            PropertySpec
                .builder("id", INT, KModifier.OVERRIDE)
                .initializer("%L", metadata.id)
                .build(),
        )
        addProperty(
            PropertySpec
                .builder("qualifier", STRING, KModifier.OVERRIDE)
                .initializer("%S", metadata.qualifier)
                .build(),
        )
        addProperty(
            PropertySpec
                .builder("function", STRING, KModifier.OVERRIDE)
                .initializer("%S", metadata.function)
                .build(),
        )

        addProperty(
            PropertySpec
                .builder("rateLimits", RateLimits::class, KModifier.OVERRIDE)
                .initializer(
                    if (metadata.rateLimits.period > 0 || metadata.rateLimits.rate > 0) {
                        CodeBlock.of("RateLimits(%LL, %LL)", metadata.rateLimits.period, metadata.rateLimits.rate)
                    } else {
                        CodeBlock.of("%T.NOT_LIMITED", RateLimits::class)
                    },
                ).build(),
        )

        if (metadata.guardClass != DefaultGuard::class.fqName) {
            addProperty(
                PropertySpec
                    .builder(
                        "guardClass",
                        TypeConstants.kClass
                            .parameterizedBy(WildcardTypeName.producerOf(TypeConstants.guard)),
                        KModifier.OVERRIDE,
                    ).initializer("%L::class", metadata.guardClass)
                    .build(),
            )
        }

        if (metadata.argParserClass != DefaultArgParser::class.fqName) {
            addProperty(
                PropertySpec
                    .builder(
                        "argParser",
                        TypeConstants.kClass
                            .parameterizedBy(WildcardTypeName.producerOf(TypeConstants.argumentParser)),
                        KModifier.OVERRIDE,
                    ).initializer("%L::class", metadata.argParserClass)
                    .build(),
            )
        }
    }
}

/**
 * Extension function to create ActivityMetadata from a function declaration.
 */
fun KSFunctionDeclaration.toActivityMetadata(
    rateLimits: RateLimits = RateLimits.NOT_LIMITED,
    guardClass: String? = null,
    argParserClass: String? = null,
): ActivityMetadata {
    val funQualifier = qualifiedName!!.getQualifier()
    val funShortName = simpleName.getShortName()

    return ActivityMetadata(
        id = getActivityId(),
        qualifier = funQualifier,
        function = funShortName,
        rateLimits = rateLimits,
        guardClass = guardClass ?: DefaultGuard::class.fqName,
        argParserClass = argParserClass ?: DefaultArgParser::class.fqName,
    )
}
