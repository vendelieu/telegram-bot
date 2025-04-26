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
import eu.vendeli.ksp.dto.CommandHandlerParams.CallbackQueryAutoAnswer
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.utils.addMap
import eu.vendeli.ksp.utils.buildMeta
import eu.vendeli.ksp.utils.checkForInapplicableAnnotations
import eu.vendeli.ksp.utils.commonMatcherClass
import eu.vendeli.ksp.utils.invocableType
import eu.vendeli.ksp.utils.parseAnnotatedArgParser
import eu.vendeli.ksp.utils.parseAnnotatedGuard
import eu.vendeli.ksp.utils.parseAnnotatedRateLimits
import eu.vendeli.ksp.utils.parseAsCommandHandler
import eu.vendeli.ksp.utils.parseAsInputHandler
import eu.vendeli.ksp.utils.parseAsUpdateHandler
import eu.vendeli.ksp.utils.toRateLimits
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.RateLimits
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.component.UpdateType

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

        // priority while looking for util annotations: function > class > handler param
        val guardAnnotationData = function.parseAnnotatedGuard()
        val rateLimitsAnnotationData = function.parseAnnotatedRateLimits()
        val argParserAnnotationData = function.parseAnnotatedArgParser()

        val params = if (
            // if annotation have autoAnswer == true
            annotationData.isAutoAnswer == true || autoAnswerCallback && annotationData.isAutoAnswer != false
            // or autoAnswerCallback == true in plugin options and annotation have autoAnswer == null/true
        ) listOf(
            CallbackQueryAutoAnswer,
        ) else emptyList()

        annotationData.value.forEach {
            annotationData.scope.forEach { updT ->
                logger.info("Command: $it UpdateType: ${updT.name} --> ${function.qualifiedName?.asString()}")

                addStatement(
                    "(\"$it\" to %L) to %L,",
                    updT,
                    activitiesFile.buildInvocationLambdaCodeBlock(
                        function,
                        injectableTypes,
                        ctx,
                        buildMeta(
                            qualifier = function.qualifiedName!!.getQualifier(),
                            function = function.simpleName.asString(),
                            rateLimits = rateLimitsAnnotationData ?: annotationData.rateLimits.toRateLimits(),
                            guardClass = guardAnnotationData ?: annotationData.guardClass,
                            argParserClass = argParserAnnotationData ?: annotationData.argParserClass,
                        ),
                        params,
                        updateType = updT,
                    ),
                )
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

        // priority while looking for util annotations: function > class > handler param
        val guardAnnotationData = function.parseAnnotatedGuard()
        val rateLimitsAnnotationData = function.parseAnnotatedRateLimits()

        function.checkForInapplicableAnnotations(
            "InputHandler",
            logger,
            ArgParser::class.simpleName!!,
        )

        annotationData.first.forEach {
            logger.info("Input: $it --> ${function.qualifiedName?.asString()}")

            addStatement(
                "\"$it\" to %L,",
                activitiesFile.buildInvocationLambdaCodeBlock(
                    function,
                    injectableTypes,
                    ctx,
                    buildMeta(
                        qualifier = function.qualifiedName!!.getQualifier(),
                        function = function.simpleName.asString(),
                        rateLimits = rateLimitsAnnotationData ?: annotationData.second.toRateLimits(),
                        guardClass = guardAnnotationData ?: annotationData.third,
                        argParserClass = null,
                    ),
                ),
            )
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

        function.checkForInapplicableAnnotations(
            "UpdateHandler",
            logger,
            Guard::class.simpleName!!,
            ArgParser::class.simpleName!!,
            RateLimits::class.simpleName!!,
        )

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
                            data.forEach { commonAnnotationData ->
                                // priority while looking for util annotations: function > class > handler param
                                commonAnnotationData.funDeclaration.checkForInapplicableAnnotations(
                                    "CommonHandler",
                                    logger,
                                    Guard::class.simpleName!!,
                                )
                                val rateLimitsAnnotationData =
                                    commonAnnotationData.funDeclaration.parseAnnotatedRateLimits()
                                val argParserAnnotationData =
                                    commonAnnotationData.funDeclaration.parseAnnotatedArgParser()

                                addStatement(
                                    "%L to %L,",
                                    commonAnnotationData.value.toCommonMatcher(
                                        commonAnnotationData.filter,
                                        commonAnnotationData.scope,
                                    ),
                                    activitiesFile.buildInvocationLambdaCodeBlock(
                                        commonAnnotationData.funDeclaration,
                                        injectableTypes,
                                        ctx,
                                        buildMeta(
                                            qualifier = commonAnnotationData.funQualifier,
                                            function = commonAnnotationData.funSimpleName,
                                            rateLimits = rateLimitsAnnotationData ?: commonAnnotationData.rateLimits,
                                            argParserClass = argParserAnnotationData ?: commonAnnotationData.argParser,
                                            guardClass = null,
                                        ),
                                    ),
                                )
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
    unprocessedHandlerSymbols?.checkForInapplicableAnnotations(
        "UnprocessedHandler",
        logger,
        Guard::class.simpleName!!,
        RateLimits::class.simpleName!!,
        ArgParser::class.simpleName!!,
    )

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
