package eu.vendeli.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.ChannelPostUpdate
import eu.vendeli.tgbot.types.internal.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.internal.ChatMemberUpdate
import eu.vendeli.tgbot.types.internal.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.internal.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.internal.EditedMessageUpdate
import eu.vendeli.tgbot.types.internal.InlineQueryUpdate
import eu.vendeli.tgbot.types.internal.MessageReactionCountUpdate
import eu.vendeli.tgbot.types.internal.MessageReactionUpdate
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.MyChatMemberUpdate
import eu.vendeli.tgbot.types.internal.PollAnswerUpdate
import eu.vendeli.tgbot.types.internal.PollUpdate
import eu.vendeli.tgbot.types.internal.PreCheckoutQueryUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate
import kotlin.reflect.KClass

@Suppress("LongMethod", "CyclomaticComplexMethod")
internal fun FileBuilder.buildInvocationLambdaCodeBlock(
    function: KSFunctionDeclaration,
    injectableTypes: Map<TypeName, ClassName>,
) = buildCodeBlock {
    val isTopLvl = function.functionKind == FunctionKind.TOP_LEVEL
    val funQualifier = function.qualifiedName!!.getQualifier()
    val funName = if (!isTopLvl) {
        funQualifier.let { it + "::" + function.simpleName.getShortName() }
    } else {
        addImport(funQualifier, function.simpleName.getShortName())
        "::${function.simpleName.getShortName()}"
    }
    val isObject = (function.parent as? KSClassDeclaration)?.classKind == ClassKind.OBJECT

    beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->").apply {
        var parametersEnumeration = ""
        if (!isTopLvl && !isObject && function.functionKind != FunctionKind.STATIC) {
            parametersEnumeration = "inst, "
            add(
                "val inst = classManager.getInstance(%L::class.java) as %L\n",
                funQualifier,
                funQualifier,
            )
        }
        function.parameters.forEachIndexed { index, parameter ->
            if (parameter.name == null) return@forEachIndexed
            val paramCall = (
                parameter.annotations.firstOrNull { i ->
                    i.shortName.asString() == "ParamMapping"
                }?.let { i ->
                    i.arguments.first { a -> a.name?.asString() == "name" }.value as? String
                } ?: parameter.name!!.getShortName()
            ).let {
                "parameters[\"$it\"]"
            }
            val typeName = parameter.type.toTypeName()
            val nullabilityMark = if (typeName.isNullable) "" else "!!"

            val value = when (typeName.copy(false)) {
                userClass -> "user$nullabilityMark"
                botClass -> "bot"
                STRING -> "$paramCall.toString()"
                INT, intPrimitiveType -> "$paramCall?.toIntOrNull()$nullabilityMark"
                LONG, longPrimitiveType -> "$paramCall?.toLongOrNull()$nullabilityMark"
                SHORT, shortPrimitiveType -> "$paramCall?.toShortOrNull()$nullabilityMark"
                FLOAT, floatPrimitiveType -> "$paramCall?.toFloatOrNull()$nullabilityMark"
                DOUBLE, doublePrimitiveType -> "$paramCall?.toDoubleOrNull()$nullabilityMark"

                updateClass -> "update"
                messageUpdClass -> addUpdate(MessageUpdate::class, nullabilityMark)
                callbackQueryUpdateClass -> addUpdate(CallbackQueryUpdate::class, nullabilityMark)
                editedMessageUpdateClass -> addUpdate(EditedMessageUpdate::class, nullabilityMark)
                channelPostUpdateClass -> addUpdate(ChannelPostUpdate::class, nullabilityMark)
                editedChannelPostUpdate -> addUpdate(EditedChannelPostUpdate::class, nullabilityMark)
                messageReactionUpdate -> addUpdate(MessageReactionUpdate::class, nullabilityMark)
                messageReactionCountUpdate -> addUpdate(MessageReactionCountUpdate::class, nullabilityMark)
                inlineQueryUpdateClass -> addUpdate(InlineQueryUpdate::class, nullabilityMark)
                chosenInlineResultUpdateClass -> addUpdate(ChosenInlineResultUpdate::class, nullabilityMark)
                shippingQueryUpdateClass -> addUpdate(ShippingQueryUpdate::class, nullabilityMark)
                preCheckoutQueryUpdateClass -> addUpdate(PreCheckoutQueryUpdate::class, nullabilityMark)
                pollUpdateClass -> addUpdate(PollUpdate::class, nullabilityMark)
                pollAnswerUpdateClass -> addUpdate(PollAnswerUpdate::class, nullabilityMark)
                myChatMemberUpdateClass -> addUpdate(MyChatMemberUpdate::class, nullabilityMark)
                chatMemberUpdateClass -> addUpdate(ChatMemberUpdate::class, nullabilityMark)
                chatJoinRequestUpdateClass -> addUpdate(ChatJoinRequestUpdate::class, nullabilityMark)

                in injectableTypes.keys -> {
                    val type = injectableTypes[typeName]!!
                    addImport(type.packageName, type.simpleName)
                    "(classManager.getInstance(${type.simpleName}::class.java) as ${type.simpleName}).get(update, bot)"
                }

                else -> "null"
            }
            add("val param%L = %L\n", index, value)
            parametersEnumeration += "param$index"
            if (index < function.parameters.lastIndex) parametersEnumeration += ", "
        }
        add("%L.invoke(\n\t%L\n)\n", funName, parametersEnumeration)
    }.endControlFlow().build()
}

private fun <T : ProcessedUpdate> FileBuilder.addUpdate(
    `class`: KClass<T>,
    nullabilityMark: String,
): String {
    addImport("eu.vendeli.tgbot.types.internal", `class`.simpleName!!)
    return "(update as? ${`class`.simpleName})$nullabilityMark"
}
