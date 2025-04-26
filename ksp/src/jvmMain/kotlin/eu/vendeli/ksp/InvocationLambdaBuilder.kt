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
import eu.vendeli.ksp.dto.CollectorsContext
import eu.vendeli.ksp.dto.CommandHandlerParams
import eu.vendeli.ksp.dto.LambdaParameters
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.botClass
import eu.vendeli.ksp.utils.businessConnectionUpdateClass
import eu.vendeli.ksp.utils.businessMessageUpdateClass
import eu.vendeli.ksp.utils.callbackQueryUpdateClass
import eu.vendeli.ksp.utils.channelPostUpdateClass
import eu.vendeli.ksp.utils.chatBoostUpdateClass
import eu.vendeli.ksp.utils.chatJoinRequestUpdateClass
import eu.vendeli.ksp.utils.chatMemberUpdateClass
import eu.vendeli.ksp.utils.chosenInlineResultUpdateClass
import eu.vendeli.ksp.utils.deletedBusinessMessagesClass
import eu.vendeli.ksp.utils.doublePrimitiveType
import eu.vendeli.ksp.utils.editedBusinessMessageClass
import eu.vendeli.ksp.utils.editedChannelPostUpdateClass
import eu.vendeli.ksp.utils.editedMessageUpdateClass
import eu.vendeli.ksp.utils.floatPrimitiveType
import eu.vendeli.ksp.utils.inlineQueryUpdateClass
import eu.vendeli.ksp.utils.intPrimitiveType
import eu.vendeli.ksp.utils.longPrimitiveType
import eu.vendeli.ksp.utils.messageReactionCountUpdateClass
import eu.vendeli.ksp.utils.messageReactionUpdateClass
import eu.vendeli.ksp.utils.messageUpdClass
import eu.vendeli.ksp.utils.myChatMemberUpdateClass
import eu.vendeli.ksp.utils.pollAnswerUpdateClass
import eu.vendeli.ksp.utils.pollUpdateClass
import eu.vendeli.ksp.utils.preCheckoutQueryUpdateClass
import eu.vendeli.ksp.utils.purchasedPaidMediaUpdateClass
import eu.vendeli.ksp.utils.removedChatBoostUpdateClass
import eu.vendeli.ksp.utils.shippingQueryUpdateClass
import eu.vendeli.ksp.utils.shortPrimitiveType
import eu.vendeli.ksp.utils.updateClass
import eu.vendeli.ksp.utils.userClass
import eu.vendeli.tgbot.types.component.BusinessConnectionUpdate
import eu.vendeli.tgbot.types.component.BusinessMessageUpdate
import eu.vendeli.tgbot.types.component.CallbackQueryUpdate
import eu.vendeli.tgbot.types.component.ChannelPostUpdate
import eu.vendeli.tgbot.types.component.ChatBoostUpdate
import eu.vendeli.tgbot.types.component.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.component.ChatMemberUpdate
import eu.vendeli.tgbot.types.component.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.component.DeletedBusinessMessagesUpdate
import eu.vendeli.tgbot.types.component.EditedBusinessMessageUpdate
import eu.vendeli.tgbot.types.component.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.component.EditedMessageUpdate
import eu.vendeli.tgbot.types.component.InlineQueryUpdate
import eu.vendeli.tgbot.types.component.MessageReactionCountUpdate
import eu.vendeli.tgbot.types.component.MessageReactionUpdate
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.MyChatMemberUpdate
import eu.vendeli.tgbot.types.component.PollAnswerUpdate
import eu.vendeli.tgbot.types.component.PollUpdate
import eu.vendeli.tgbot.types.component.PreCheckoutQueryUpdate
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.PurchasedPaidMediaUpdate
import eu.vendeli.tgbot.types.component.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.component.ShippingQueryUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import kotlin.reflect.KClass

@Suppress("LongMethod", "CyclomaticComplexMethod")
internal fun FileBuilder.buildInvocationLambdaCodeBlock(
    function: KSFunctionDeclaration,
    injectableTypes: Map<TypeName, ClassName>,
    ctx: CollectorsContext? = null,
    meta: Pair<String, Array<Any?>>? = null,
    parameters: List<LambdaParameters> = emptyList(),
    updateType: UpdateType? = null,
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

    val lambda = meta?.let {
        beginControlFlow(
            "suspendCall(\n\t${it.first}\n) { classManager, update, user, bot, parameters ->",
            *it.second,
        )
    } ?: beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->")

    lambda
        .apply {
            var parametersEnumeration = ""
            if (!isTopLvl && !isObject && function.functionKind != FunctionKind.STATIC) {
                parametersEnumeration = "inst, "
                add(
                    "val inst = classManager.getInstance<%L>()!!\n",
                    funQualifier,
                )
            }
            var isUserNullable = true
            function.parameters.forEachIndexed { index, parameter ->
                if (parameter.name == null) return@forEachIndexed
                val paramCall = (
                    parameter.annotations
                        .firstOrNull { i ->
                            i.shortName.asString() == "ParamMapping"
                        }?.let { i ->
                            i.arguments.first { a -> a.name?.asString() == "name" }.value as? String
                        } ?: parameter.name!!.getShortName()
                ).let {
                    "parameters[\"$it\"]"
                }
                val parameterTypeName = parameter.type.toTypeName()
                val typeName = parameterTypeName.copy(false)
                val nullabilityMark = if (parameterTypeName.isNullable) "" else "!!"

                val value = when (typeName) {
                    userClass -> "user$nullabilityMark".also {
                        if (!parameterTypeName.isNullable) isUserNullable = false
                    }

                    botClass -> "bot"
                    STRING -> "$paramCall$nullabilityMark"
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
                    editedChannelPostUpdateClass -> addUpdate(EditedChannelPostUpdate::class, nullabilityMark)
                    messageReactionUpdateClass -> addUpdate(MessageReactionUpdate::class, nullabilityMark)
                    messageReactionCountUpdateClass -> addUpdate(MessageReactionCountUpdate::class, nullabilityMark)
                    inlineQueryUpdateClass -> addUpdate(InlineQueryUpdate::class, nullabilityMark)
                    chosenInlineResultUpdateClass -> addUpdate(ChosenInlineResultUpdate::class, nullabilityMark)
                    shippingQueryUpdateClass -> addUpdate(ShippingQueryUpdate::class, nullabilityMark)
                    preCheckoutQueryUpdateClass -> addUpdate(PreCheckoutQueryUpdate::class, nullabilityMark)
                    pollUpdateClass -> addUpdate(PollUpdate::class, nullabilityMark)
                    pollAnswerUpdateClass -> addUpdate(PollAnswerUpdate::class, nullabilityMark)
                    myChatMemberUpdateClass -> addUpdate(MyChatMemberUpdate::class, nullabilityMark)
                    chatMemberUpdateClass -> addUpdate(ChatMemberUpdate::class, nullabilityMark)
                    chatJoinRequestUpdateClass -> addUpdate(ChatJoinRequestUpdate::class, nullabilityMark)
                    chatBoostUpdateClass -> addUpdate(ChatBoostUpdate::class, nullabilityMark)
                    removedChatBoostUpdateClass -> addUpdate(RemovedChatBoostUpdate::class, nullabilityMark)
                    businessConnectionUpdateClass -> addUpdate(BusinessConnectionUpdate::class, nullabilityMark)
                    businessMessageUpdateClass -> addUpdate(BusinessMessageUpdate::class, nullabilityMark)
                    editedBusinessMessageClass -> addUpdate(EditedBusinessMessageUpdate::class, nullabilityMark)
                    deletedBusinessMessagesClass -> addUpdate(DeletedBusinessMessagesUpdate::class, nullabilityMark)
                    purchasedPaidMediaUpdateClass -> addUpdate(PurchasedPaidMediaUpdate::class, nullabilityMark)

                    in injectableTypes.keys -> {
                        val type = injectableTypes[typeName]!!
                        "classManager.getInstance<${type.canonicalName}>()!!.get(update, bot)"
                    }

                    else -> "null"
                }
                add("val param%L = %L\n", index, value)
                parametersEnumeration += "param$index"
                if (index < function.parameters.lastIndex) parametersEnumeration += ", "
            }

            if (ctx?.autoCleanClassData == true && ctx.pkg != null) add(
                "\nif (\n\t" +
                    (if (isUserNullable) "user != null\n && " else "") +
                    "bot.update.userClassSteps[user.id] != %S\n) %L.____clearClassData(user.id)\n",
                funQualifier,
                ctx.pkg,
            )

            if (updateType == UpdateType.CALLBACK_QUERY &&
                parameters.contains(CommandHandlerParams.CallbackQueryAutoAnswer)
            ) {
                addImport("eu.vendeli.tgbot.api.answer", "answerCallbackQuery")
                addImport("eu.vendeli.tgbot.types.component", "CallbackQueryUpdate", "getUser")
                add("answerCallbackQuery((update as CallbackQueryUpdate).callbackQuery.id).send(update.getUser(), bot)")
            }

            add("\n%L.invoke(\n\t%L\n)\n", funName, parametersEnumeration)
        }.endControlFlow()
        .build()
}

private fun <T : ProcessedUpdate> FileBuilder.addUpdate(
    kClass: KClass<T>,
    nullabilityMark: String,
): String {
    addImport("eu.vendeli.tgbot.types.component", kClass.simpleName!!)
    return "(update as? ${kClass.simpleName})$nullabilityMark"
}
