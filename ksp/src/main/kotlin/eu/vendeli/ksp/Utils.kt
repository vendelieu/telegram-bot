@file:Suppress("LongParameterList", "LongMethod", "CyclomaticComplexMethod")
package eu.vendeli.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits

internal fun FileSpec.Builder.collectActions(
    logger: KSPLogger,
    autowiringTypes: Map<TypeName, ClassName>,
    commandHandlerSymbols: Sequence<KSFunctionDeclaration>,
    inputHandlerSymbols: Sequence<KSFunctionDeclaration>,
    regexCommandHandlerSymbols: Sequence<KSFunctionDeclaration>,
    updateHandlerSymbols: Sequence<KSFunctionDeclaration>,
    unprocessedHandlerSymbols: KSFunctionDeclaration?,
) {
    logger.info("Collecting commands.")
    addMap(
        "`TG_\$COMMANDS`",
        MAP.parameterizedBy(
            Pair::class.asTypeName().parameterizedBy(STRING, UpdateType::class.asTypeName()),
            invocableType,
        ),
        commandHandlerSymbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == "CommandHandler"
        }.arguments.parseToCommandHandlerArgs()

        annotationData.first.forEach {
            annotationData.third.forEach { updT ->
                logger.info("Command: $it UpdateType: $updT --> ${function.qualifiedName?.asString()}")

                addImport(UpdateType::class, updT.name)
                addStatement(
                    "(\"$it\" to %L) to (%L to %L),",
                    updT,
                    buildInvocationLambdaCodeBlock(
                        function,
                        autowiringTypes,
                    ),
                    InvocationMeta(
                        "\"" + function.qualifiedName!!.getQualifier() + "\"",
                        "\"" + function.simpleName.asString() + "\"",
                        annotationData.second.toRateLimits(),
                    ),
                )
            }
        }
    }

    logger.info("Collecting inputs.")
    addMap(
        "`TG_\$INPUTS`",
        MAP.parameterizedBy(
            STRING,
            invocableType,
        ),
        inputHandlerSymbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == "InputHandler"
        }.arguments.parseToInputHandlerArgs()
        annotationData.first.forEach {
            logger.info("Input: $it --> ${function.qualifiedName?.asString()}")
            addStatement(
                "\"$it\" to (%L to %L),",
                buildInvocationLambdaCodeBlock(
                    function,
                    autowiringTypes,
                ),
                InvocationMeta(
                    "\"" + function.qualifiedName!!.getQualifier() + "\"",
                    "\"" + function.simpleName.asString() + "\"",
                    annotationData.second.toRateLimits(),
                ),
            )
        }
    }

    logger.info("Collecting regex handlers.")
    addMap(
        "`TG_\$REGEX`",
        MAP.parameterizedBy(
            Regex::class.asTypeName(),
            invocableType,
        ),
        regexCommandHandlerSymbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == "RegexCommandHandler"
        }.arguments.parseToRegexHandlerArgs()
        addStatement(
            "Regex(\"%L\") to (%L to %L),",
            annotationData.first,
            buildInvocationLambdaCodeBlock(
                function,
                autowiringTypes,
            ),
            InvocationMeta(
                "\"" + function.qualifiedName!!.getQualifier() + "\"",
                "\"" + function.simpleName.asString() + "\"",
                annotationData.second.toRateLimits(),
            ),
        )
        logger.info("Regex: ${annotationData.first} --> ${function.qualifiedName?.asString()}")
    }

    logger.info("Collecting `UpdateType` handlers.")
    addMap(
        "`TG_\$UPDATE_TYPES`",
        MAP.parameterizedBy(
            UpdateType::class.asTypeName(),
            TypeVariableName("InvocationLambda"),
        ),
        updateHandlerSymbols,
    ) { function ->
        val annotationData = function.annotations.first {
            it.shortName.asString() == "UpdateHandler"
        }.arguments.parseToUpdateHandlerArgs()

        annotationData.forEach {
            logger.info("UpdateType: $it --> ${function.qualifiedName?.asString()}")
            addImport(UpdateType::class, it.name)
            addStatement(
                "%L to %L,",
                it,
                buildInvocationLambdaCodeBlock(
                    function,
                    autowiringTypes,
                ),
            )
        }
    }

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
                            buildInvocationLambdaCodeBlock(it, autowiringTypes)
                        },
                    )
                },
            )
        }.build(),
    )

    addProperty(
        PropertySpec.builder(
            "\$ACTIONS",
            List::class.asTypeName().parameterizedBy(ANY.copy(true)),
            KModifier.INTERNAL,
        ).apply {
            initializer(
                "%L",
                "listOf(`TG_\$COMMANDS`, `TG_\$INPUTS`, `TG_\$REGEX`, `TG_\$UPDATE_TYPES`, `TG_\$UNPROCESSED`)",
            )
        }.build(),
    )
}

private fun Pair<Long, Long>.toRateLimits() = RateLimits(first, second)

private fun FileSpec.Builder.addMap(
    name: String,
    type: ParameterizedTypeName,
    symbols: Sequence<KSFunctionDeclaration>,
    iterableAction: CodeBlock.Builder.(KSFunctionDeclaration) -> Unit,
) = addProperty(
    PropertySpec.builder(
        name,
        type,
        KModifier.PRIVATE,
    ).apply {
        initializer(
            CodeBlock.builder().apply {
                add("mapOf(\n")
                symbols.forEach {
                    iterableAction(it)
                }
                add(")\n")
            }.build(),
        )
    }.build(),
)

private fun FileSpec.Builder.buildInvocationLambdaCodeBlock(
    function: KSFunctionDeclaration,
    autowiringTypes: Map<TypeName, ClassName>,
) = buildCodeBlock {
    val funQualifier = function.qualifiedName!!.getQualifier()
    val funName = funQualifier.let { it + "::" + function.simpleName.getShortName() }

    beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->").apply {
        add(
            "val inst = classManager.getInstance(%L::class.java) as %L\n",
            funQualifier,
            funQualifier,
        )
        var parametersEnumeration = if (function.parameters.isNotEmpty()) ", " else ""
        function.parameters.forEachIndexed { index, parameter ->
            val paramCall = (
                parameter.annotations.firstOrNull { i ->
                    i.shortName.asString() == "ParamMapping"
                }?.let { i ->
                    i.arguments.first { a -> a.name?.asString() == "name" }.value as? String
                } ?: parameter.name!!.getShortName()
            ).let {
                "parameters[\"$it\"]"
            }
            val typeName = parameter.type.resolve().toTypeName()
            val nullabilityMark = if (typeName.isNullable) "" else "!!"
            val value = when (typeName) {
                userClass -> "user$nullabilityMark"
                botClass -> "bot"
                STRING -> "$paramCall.toString()"
                INT, intPrimitiveType -> "$paramCall?.toIntOrNull()$nullabilityMark"
                LONG, longPrimitiveType -> "$paramCall?.toLongOrNull()$nullabilityMark"
                SHORT, shortPrimitiveType -> "$paramCall?.toShortOrNull()$nullabilityMark"
                FLOAT, floatPrimitiveType -> "$paramCall?.toFloatOrNull()$nullabilityMark"
                DOUBLE, doublePrimitiveType -> "$paramCall?.toDoubleOrNull()$nullabilityMark"

                updateClass -> "update"
                messageUpdClass -> "(update as? MessageUpdate)$nullabilityMark"
                callbackQueryUpdateClass -> "(update as? CallbackQueryUpdate)$nullabilityMark"
                editedMessageUpdateClass -> "(update as? EditedMessageUpdate)$nullabilityMark"
                channelPostUpdateClass -> "(update as? ChannelPostUpdate)$nullabilityMark"
                editedChannelPostUpdate -> "(update as? EditedChannelPostUpdate)$nullabilityMark"
                inlineQueryUpdateClass -> "(update as? InlineQueryUpdate)$nullabilityMark"
                chosenInlineResultUpdateClass -> "(update as? ChosenInlineResultUpdate)$nullabilityMark"
                shippingQueryUpdateClass -> "(update as? ShippingQueryUpdate)$nullabilityMark"
                preCheckoutQueryUpdateClass -> "(update as? PreCheckoutQueryUpdate)$nullabilityMark"
                pollUpdateClass -> "(update as? PollUpdate)$nullabilityMark"
                pollAnswerUpdateClass -> "(update as? PollAnswerUpdate)$nullabilityMark"
                myChatMemberUpdateClass -> "(update as? MyChatMemberUpdate)$nullabilityMark"
                chatMemberUpdateClass -> "(update as? ChatMemberUpdate)$nullabilityMark"
                chatJoinRequestUpdateClass -> "(update as? ChatJoinRequestUpdate)$nullabilityMark"

                in autowiringTypes.keys -> {
                    val type = autowiringTypes[typeName]!!
                    addImport(type.packageName, type.simpleName)
                    "(classManager.getInstance(${type.simpleName}::class.java) as ${type.simpleName}).get(update, bot)"
                }

                else -> "null"
            }
            add("val param%L = %L\n", index, value)
            parametersEnumeration += "param$index"
            if (index < function.parameters.lastIndex) parametersEnumeration += ", "
        }
        add(
            "return@suspendCall %L.invoke(\n\tinst%L\n)\n",
            funName,
            parametersEnumeration,
        )
    }.endControlFlow().build()
}
