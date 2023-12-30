package eu.vendeli.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.RegexCommandHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.UpdateType

internal fun FileBuilder.collectCommandActions(
    symbols: Sequence<KSFunctionDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
) {
    logger.info("Collecting commands.")
    addMap(
        "`TG_\$COMMANDS`",
        MAP.parameterizedBy(
            Pair::class.asTypeName().parameterizedBy(STRING, UpdateType::class.asTypeName()),
            invocableType,
        ),
        symbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() in listOf(
                CommandHandler::class.simpleName,
                CallbackQuery::class.simpleName,
            )
        }.arguments.parseAsCommandHandler()

        annotationData.first.forEach {
            annotationData.third.forEach { updT ->
                logger.info("Command: $it UpdateType: ${updT.name} --> ${function.qualifiedName?.asString()}")

                addImport(UpdateType::class, updT.name)
                addStatement(
                    "(\"$it\" to %L) to (%L to InvocationMeta(\"%L\", \"%L\", %L)),",
                    updT.name,
                    buildInvocationLambdaCodeBlock(function, injectableTypes),
                    function.qualifiedName!!.getQualifier(),
                    function.simpleName.asString(),
                    annotationData.second.toRateLimits(),
                )
            }
        }
    }
}

internal fun FileBuilder.collectInputActions(
    symbols: Sequence<KSFunctionDeclaration>,
    chainSymbols: Sequence<KSClassDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
) {
    logger.info("Collecting inputs.")
    val tailBlock = collectInputChains(chainSymbols, logger)

    addMap(
        "`TG_\$INPUTS`",
        MAP.parameterizedBy(STRING, invocableType),
        symbols,
        tailBlock,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == InputHandler::class.simpleName!!
        }.arguments.parseAsInputHandler()
        annotationData.first.forEach {
            logger.info("Input: $it --> ${function.qualifiedName?.asString()}")
            addStatement(
                "\"$it\" to (%L to InvocationMeta(\"%L\", \"%L\", %L)),",
                buildInvocationLambdaCodeBlock(function, injectableTypes),
                function.qualifiedName!!.getQualifier(),
                function.simpleName.asString(),
                annotationData.second.toRateLimits(),
            )
        }
    }
}

internal fun FileBuilder.collectRegexActions(
    symbols: Sequence<KSFunctionDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
) {
    logger.info("Collecting regex handlers.")
    addMap(
        "`TG_\$REGEX`",
        MAP.parameterizedBy(Regex::class.asTypeName(), invocableType),
        symbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == RegexCommandHandler::class.simpleName!!
        }.arguments.parseAsRegexHandler()
        addStatement(
            "Regex(\"%L\") to (%L to InvocationMeta(\"%L\", \"%L\", %L)),",
            annotationData.first,
            buildInvocationLambdaCodeBlock(function, injectableTypes),
            function.qualifiedName!!.getQualifier(),
            function.simpleName.asString(),
            annotationData.second.toRateLimits(),
        )
        logger.info("Regex: ${annotationData.first} --> ${function.qualifiedName?.asString()}")
    }
}

internal fun FileBuilder.collectUpdateTypeActions(
    symbols: Sequence<KSFunctionDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
) {
    logger.info("Collecting `UpdateType` handlers.")
    addMap(
        "`TG_\$UPDATE_TYPES`",
        MAP.parameterizedBy(UpdateType::class.asTypeName(), TypeVariableName("InvocationLambda")),
        symbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == UpdateHandler::class.simpleName!!
        }.arguments.parseAsUpdateHandler()

        annotationData.forEach {
            logger.info("UpdateType: ${it.name} --> ${function.qualifiedName?.asString()}")
            addImport(UpdateType::class, it.name)
            addStatement(
                "%L to %L,",
                it.name,
                buildInvocationLambdaCodeBlock(function, injectableTypes),
            )
        }
    }
}

internal fun FileBuilder.collectUnprocessed(
    unprocessedHandlerSymbols: KSFunctionDeclaration?,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
) {
    addProperty(
        PropertySpec.builder(
            "`TG_\$UNPROCESSED`",
            TypeVariableName("InvocationLambda").copy(true),
            KModifier.PRIVATE,
        ).apply {
            initializer(
                buildCodeBlock {
                    add(
                        "%L",
                        unprocessedHandlerSymbols?.let {
                            logger.info("Unprocessed handler --> ${it.qualifiedName?.asString()}")
                            buildInvocationLambdaCodeBlock(it, injectableTypes)
                        },
                    )
                },
            )
        }.build(),
    )
}
