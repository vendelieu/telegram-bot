package eu.vendeli.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.utils.addMap
import eu.vendeli.ksp.utils.addVarStatement
import eu.vendeli.ksp.utils.commonMatcherClass
import eu.vendeli.ksp.utils.invocableType
import eu.vendeli.ksp.utils.parseAsCommandHandler
import eu.vendeli.ksp.utils.parseAsInputHandler
import eu.vendeli.ksp.utils.parseAsUpdateHandler
import eu.vendeli.ksp.utils.toRateLimits
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.fullName

internal fun collectCommandActivities(
    symbols: Sequence<KSFunctionDeclaration>,
    ctx: CollectorsContext,
) = ctx.run {
    logger.info("Collecting commands.")
    activitiesFile.addMap(
        "__TG_COMMANDS$idxPostfix",
        MAP.parameterizedBy(
            Pair::class.asTypeName().parameterizedBy(STRING, UpdateType::class.asTypeName()),
            invocableType,
        ),
        symbols,
    ) { function ->
        var isCallbackQAnnotation = false
        val annotationData = function.annotations
            .first {
                val shortName = it.shortName.asString()
                when (shortName) {
                    CallbackQuery::class.simpleName -> {
                        isCallbackQAnnotation = true
                        true
                    }

                    CommandHandler::class.simpleName -> true
                    else -> false
                }
            }.arguments
            .parseAsCommandHandler(isCallbackQAnnotation)

        annotationData.value.forEach {
            annotationData.scope.forEach { updT ->
                logger.info("Command: $it UpdateType: ${updT.name} --> ${function.qualifiedName?.asString()}")

                addVarStatement(postFix = "\n)),") {
                    add("(\"$it\" to %L)" to updT)
                    add(
                        " to (%L to InvocationMeta(\n" to activitiesFile.buildInvocationLambdaCodeBlock(
                            function,
                            injectableTypes,
                            pkg,
                        ),
                    )
                    add("qualifier = \"%L\"" to function.qualifiedName!!.getQualifier())
                    add(",\n function = \"%L\"" to function.simpleName.asString())
                    if (annotationData.rateLimits.first > 0 || annotationData.rateLimits.second > 0)
                        add(",\n rateLimits = %L" to annotationData.rateLimits.toRateLimits())
                    if (annotationData.guardClass != DefaultGuard::class.fullName)
                        add(",\n guard = %L::class" to annotationData.guardClass)
                    if (annotationData.argParserClass != DefaultArgParser::class.fullName)
                        add(",\n argParser = %L::class" to annotationData.argParserClass)
                }
            }
        }
    }
}

internal fun collectInputActivities(
    symbols: Sequence<KSFunctionDeclaration>,
    chainSymbols: Sequence<KSClassDeclaration>,
    ctx: CollectorsContext,
) = ctx.run {
    logger.info("Collecting inputs.")
    val tailBlock = collectInputChains(chainSymbols, ctx)

    activitiesFile.addMap(
        "__TG_INPUTS$idxPostfix",
        MAP.parameterizedBy(STRING, invocableType),
        symbols,
        tailBlock,
    ) { function ->
        val annotationData = function.annotations
            .first {
                it.shortName.asString() == InputHandler::class.simpleName!!
            }.arguments
            .parseAsInputHandler()
        annotationData.first.forEach {
            logger.info("Input: $it --> ${function.qualifiedName?.asString()}")
            addVarStatement(postFix = "\n)),") {
                add(
                    "\"$it\" to (%L to InvocationMeta(\n" to activitiesFile.buildInvocationLambdaCodeBlock(
                        function,
                        injectableTypes,
                        pkg,
                    ),
                )
                add("qualifier = \"%L\"" to function.qualifiedName!!.getQualifier())
                add(",\n function = \"%L\"" to function.simpleName.asString())
                if (annotationData.second.first > 0 || annotationData.second.second > 0)
                    add(",\n rateLimits = %L" to annotationData.second.toRateLimits())
                if (annotationData.third != DefaultGuard::class.fullName)
                    add(",\n guard = %L::class" to annotationData.third)
            }
        }
    }
}

internal fun collectUpdateTypeActivities(
    symbols: Sequence<KSFunctionDeclaration>,
    ctx: CollectorsContext,
) = ctx.run {
    logger.info("Collecting `UpdateType` handlers.")
    activitiesFile.addMap(
        "__TG_UPDATE_TYPES$idxPostfix",
        MAP.parameterizedBy(UpdateType::class.asTypeName(), TypeVariableName("InvocationLambda")),
        symbols,
    ) { function ->
        val annotationData = function.annotations
            .first {
                it.shortName.asString() == UpdateHandler::class.simpleName!!
            }.arguments
            .parseAsUpdateHandler()

        annotationData.forEach {
            logger.info("UpdateType: ${it.name} --> ${function.qualifiedName?.asString()}")
            addStatement(
                "%L to %L,",
                it,
                activitiesFile.buildInvocationLambdaCodeBlock(function, injectableTypes),
            )
        }
    }
}

internal fun collectCommonActivities(
    data: List<CommonAnnotationData>,
    ctx: CollectorsContext,
) = ctx.run {
    logger.info("Collecting common handlers.")
    activitiesFile.addProperty(
        PropertySpec
            .builder(
                "__TG_COMMONS$idxPostfix",
                MAP.parameterizedBy(commonMatcherClass, invocableType),
                KModifier.PRIVATE,
            ).apply {
                initializer(
                    CodeBlock
                        .builder()
                        .apply {
                            add("mapOf(\n")
                            data.forEach {
                                addVarStatement(postFix = "\n)),") {
                                    add("%L to " to it.value.toCommonMatcher(it.filter, it.scope))
                                    add(
                                        "(%L to InvocationMeta(\n" to activitiesFile.buildInvocationLambdaCodeBlock(
                                            it.funDeclaration,
                                            injectableTypes,
                                            pkg,
                                        ),
                                    )
                                    add("qualifier = \"%L\"" to it.funQualifier)
                                    add(",\n function = \"%L\"" to it.funSimpleName)
                                    if (it.rateLimits.rate > 0 || it.rateLimits.period > 0)
                                        add(",\n rateLimits = %L" to it.rateLimits)
                                    if (it.argParser != DefaultArgParser::class.fullName)
                                        add(",\n argParser = %L::class" to it.argParser)
                                }
                            }
                            add(")\n")
                        }.build(),
                )
            }.build(),
    )
}

internal fun collectUnprocessed(
    unprocessedHandlerSymbols: KSFunctionDeclaration?,
    ctx: CollectorsContext,
) = ctx.run {
    activitiesFile.addProperty(
        PropertySpec
            .builder(
                "__TG_UNPROCESSED$idxPostfix",
                TypeVariableName("InvocationLambda").copy(true),
                KModifier.PRIVATE,
            ).apply {
                initializer(
                    buildCodeBlock {
                        add(
                            "%L",
                            unprocessedHandlerSymbols?.let {
                                logger.info("Unprocessed handler --> ${it.qualifiedName?.asString()}")
                                activitiesFile.buildInvocationLambdaCodeBlock(it, injectableTypes)
                            },
                        )
                    },
                )
            }.build(),
    )
}
