@file:Suppress("TooManyFunctions")

package eu.vendeli.ksp.utils

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.ClassDataImpl
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import eu.vendeli.tgbot.interfaces.ctx.UserData
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.BusinessConnectionUpdate
import eu.vendeli.tgbot.types.internal.BusinessMessageUpdate
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.ChannelPostUpdate
import eu.vendeli.tgbot.types.internal.ChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.internal.ChatMemberUpdate
import eu.vendeli.tgbot.types.internal.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.internal.CommonMatcher
import eu.vendeli.tgbot.types.internal.DeletedBusinessMessagesUpdate
import eu.vendeli.tgbot.types.internal.EditedBusinessMessageUpdate
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
import eu.vendeli.tgbot.types.internal.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlin.reflect.KClass

internal typealias FileBuilder = FileSpec.Builder

internal val activitiesType = Map::class.asTypeName().parameterizedBy(
    STRING,
    List::class.asTypeName().parameterizedBy(ANY.copy(true)),
)
internal val invocableType = TypeVariableName("Invocable")
internal val chainLinkClass = ChainLink::class.asTypeName()
internal val autoWiringClassName = Autowiring::class.asClassName()
internal val rateLimitsClass = RateLimits::class.asTypeName()

internal val intPrimitiveType = TypeVariableName("int")
internal val longPrimitiveType = TypeVariableName("long")
internal val shortPrimitiveType = TypeVariableName("short")
internal val floatPrimitiveType = TypeVariableName("float")
internal val doublePrimitiveType = TypeVariableName("double")

internal val userClass = User::class.asTypeName()
internal val botClass = TelegramBot::class.asTypeName()

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

internal val commonMatcherClass = CommonMatcher::class.asTypeName()
internal val userDataCtx = UserData::class.asTypeName()
internal val userDataCtxDef = UserDataMapImpl::class.asTypeName()
internal val classDataCtx = ClassData::class.asTypeName()
internal val classDataCtxDef = ClassDataImpl::class.asTypeName()

internal val callbackQueryList = listOf(UpdateType.CALLBACK_QUERY)
internal val messageList = listOf(UpdateType.MESSAGE)
internal val notLimitedRateLimits = 0L to 0L

internal fun FileBuilder.addZeroLimitsProp() {
    addProperty(
        PropertySpec
            .builder(
                "zeroRateLimits",
                rateLimitsClass,
                KModifier.PRIVATE,
            ).apply {
                initializer("RateLimits(0, 0)")
            }.build(),
    )
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <R> Any?.cast(): R = this as R

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <R> Any?.safeCast(): R? = this as? R

internal fun Pair<Long, Long>.toRateLimits(): Any =
    if (first == 0L && second == 0L) "zeroRateLimits" else RateLimits(first, second)

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
