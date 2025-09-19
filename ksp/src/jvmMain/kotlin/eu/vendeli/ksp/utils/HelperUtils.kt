@file:Suppress("TooManyFunctions")

package eu.vendeli.ksp.utils

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.ClassDataImpl
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import eu.vendeli.tgbot.interfaces.ctx.UserData
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.ChainingStrategy
import eu.vendeli.tgbot.types.chain.Link
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.BusinessConnectionUpdate
import eu.vendeli.tgbot.types.component.BusinessMessageUpdate
import eu.vendeli.tgbot.types.component.CallbackQueryUpdate
import eu.vendeli.tgbot.types.component.ChannelPostUpdate
import eu.vendeli.tgbot.types.component.ChatBoostUpdate
import eu.vendeli.tgbot.types.component.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.component.ChatMemberUpdate
import eu.vendeli.tgbot.types.component.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.DeletedBusinessMessagesUpdate
import eu.vendeli.tgbot.types.component.EditedBusinessMessageUpdate
import eu.vendeli.tgbot.types.component.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.component.EditedMessageUpdate
import eu.vendeli.tgbot.types.component.IdLong
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
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.fqName
import kotlin.reflect.KClass

internal typealias FileBuilder = FileSpec.Builder

internal val activitiesType = Map::class.asTypeName().parameterizedBy(
    STRING,
    List::class.asTypeName().parameterizedBy(ANY.copy(true)),
)
internal val invocableType = TypeVariableName("Invocable")
internal val linkQName = Link::class.fqName
internal val autowiringFQName = Autowiring::class.fqName

internal val intPrimitiveType = TypeVariableName("int")
internal val longPrimitiveType = TypeVariableName("long")
internal val shortPrimitiveType = TypeVariableName("short")
internal val floatPrimitiveType = TypeVariableName("float")
internal val doublePrimitiveType = TypeVariableName("double")

internal val userClass = User::class.asTypeName()
internal val chatClass = Chat::class.asTypeName()
internal val botClass = TelegramBot::class.asTypeName()
internal val idLongClass = IdLong::class.asTypeName()

internal val updateClass = ProcessedUpdate::class.asTypeName()
internal val messageUpdClass = MessageUpdate::class.asTypeName()

internal val callbackQueryUpdateClass = CallbackQueryUpdate::class.asTypeName()
internal val editedMessageUpdateClass = EditedMessageUpdate::class.asTypeName()
internal val channelPostUpdateClass = ChannelPostUpdate::class.asTypeName()
internal val editedChannelPostUpdateClass = EditedChannelPostUpdate::class.asTypeName()
internal val messageReactionUpdateClass = MessageReactionUpdate::class.asTypeName()
internal val messageReactionCountUpdateClass = MessageReactionCountUpdate::class.asTypeName()
internal val inlineQueryUpdateClass = InlineQueryUpdate::class.asTypeName()
internal val chosenInlineResultUpdateClass = ChosenInlineResultUpdate::class.asTypeName()
internal val shippingQueryUpdateClass = ShippingQueryUpdate::class.asTypeName()
internal val preCheckoutQueryUpdateClass = PreCheckoutQueryUpdate::class.asTypeName()
internal val pollUpdateClass = PollUpdate::class.asTypeName()
internal val pollAnswerUpdateClass = PollAnswerUpdate::class.asTypeName()
internal val myChatMemberUpdateClass = MyChatMemberUpdate::class.asTypeName()
internal val chatMemberUpdateClass = ChatMemberUpdate::class.asTypeName()
internal val chatJoinRequestUpdateClass = ChatJoinRequestUpdate::class.asTypeName()
internal val chatBoostUpdateClass = ChatBoostUpdate::class.asTypeName()
internal val removedChatBoostUpdateClass = RemovedChatBoostUpdate::class.asTypeName()
internal val businessConnectionUpdateClass = BusinessConnectionUpdate::class.asTypeName()
internal val businessMessageUpdateClass = BusinessMessageUpdate::class.asTypeName()
internal val editedBusinessMessageClass = EditedBusinessMessageUpdate::class.asTypeName()
internal val deletedBusinessMessagesClass = DeletedBusinessMessagesUpdate::class.asTypeName()
internal val purchasedPaidMediaUpdateClass = PurchasedPaidMediaUpdate::class.asTypeName()

internal val commonMatcherClass = CommonMatcher::class.asTypeName()
internal val userDataCtx = UserData::class.asTypeName().parameterizedBy(STAR)
internal val userDataCtxDef = UserDataMapImpl::class.asTypeName()
internal val classDataCtx = ClassData::class.asTypeName().parameterizedBy(STAR)
internal val classDataCtxDef = ClassDataImpl::class.asTypeName()

internal val callbackQueryList = listOf(UpdateType.CALLBACK_QUERY)
internal val messageList = listOf(UpdateType.MESSAGE)
internal val notLimitedRateLimits = 0L to 0L

internal val ChainingStrategyDefault: TypeName = ChainingStrategy.Default::class.asTypeName()

@Suppress("UNCHECKED_CAST")
internal inline fun <R> Any?.cast(): R = this as R

@Suppress("UNCHECKED_CAST")
internal inline fun <R> Any?.safeCast(): R? = this as? R

internal fun Pair<Long, Long>.toRateLimits(): RateLimits = RateLimits(first, second)

fun KSType.toKSPClassName() = starProjection().let {
    if (arguments.isEmpty()) it.toClassName() else it.toTypeName()
}

internal inline fun buildMeta(
    qualifier: String,
    function: String,
    rateLimits: RateLimits,
    guardClass: String? = null,
    argParserClass: String? = null,
): Pair<String, Array<Any?>> {
    val parametersList = mutableListOf<Any?>(
        qualifier,
        function,
    )

    return buildString {
        append("InvocationMeta(\n\tqualifier = \"%L\",\n\tfunction = \"%L\"")
        if (rateLimits.period > 0 || rateLimits.rate > 0) {
            append(",\n\trateLimits = %L")
            parametersList.add(rateLimits)
        }

        if (guardClass != null && guardClass != DefaultGuard::class.fqName) {
            append(",\n\tguard = %L::class")
            parametersList.add(guardClass)
        }
        if (argParserClass != null && argParserClass != DefaultArgParser::class.fqName) {
            append(",\n\targParser = %L::class")
            parametersList.add(argParserClass)
        }
        append("\n\t)")
    } to parametersList.toTypedArray()
}

internal fun <T : Annotation> Resolver.getAnnotatedFnSymbols(
    targetPackage: String? = null,
    vararg kClasses: KClass<out T>,
): Sequence<KSFunctionDeclaration> = kClasses
    .map {
        getSymbolsWithAnnotation(it.qualifiedName!!)
    }.asSequence()
    .flatten()
    .let {
        if (targetPackage != null) {
            it
                .filter { f ->
                    f is KSFunctionDeclaration && f.packageName.asString().startsWith(targetPackage)
                }.cast()
        } else {
            it.filterIsInstance<KSFunctionDeclaration>()
        }
    }

internal fun <T : Annotation> Resolver.getAnnotatedClassSymbols(clazz: KClass<T>, targetPackage: String? = null) =
    if (targetPackage == null) getSymbolsWithAnnotation(clazz.qualifiedName!!).filterIsInstance<KSClassDeclaration>()
    else getSymbolsWithAnnotation(clazz.qualifiedName!!)
        .filter {
            it is KSClassDeclaration && it.packageName.asString().startsWith(targetPackage)
        }.cast()

internal fun FileBuilder.addMap(
    name: String,
    type: ParameterizedTypeName,
    symbols: Sequence<KSFunctionDeclaration>,
    tailBlock: CodeBlock? = null,
    iterableAction: CodeBlock.Builder.(KSFunctionDeclaration) -> Unit,
) = addProperty(
    PropertySpec
        .builder(
            name,
            type,
            KModifier.PRIVATE,
        ).apply {
            initializer(
                CodeBlock
                    .builder()
                    .apply {
                        add("mapOf(\n")
                        if (symbols.iterator().hasNext()) symbols.forEach {
                            iterableAction(it)
                        }
                        tailBlock?.also { add(it) }
                        add(")\n")
                    }.build(),
            )
        }.build(),
)
