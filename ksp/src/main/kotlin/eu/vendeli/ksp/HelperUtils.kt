package eu.vendeli.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.ChannelPostUpdate
import eu.vendeli.tgbot.types.internal.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.internal.ChatMemberUpdate
import eu.vendeli.tgbot.types.internal.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.internal.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.internal.EditedMessageUpdate
import eu.vendeli.tgbot.types.internal.InlineQueryUpdate
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.MyChatMemberUpdate
import eu.vendeli.tgbot.types.internal.PollAnswerUpdate
import eu.vendeli.tgbot.types.internal.PollUpdate
import eu.vendeli.tgbot.types.internal.PreCheckoutQueryUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlin.reflect.KClass

internal typealias FileBuilder = FileSpec.Builder

internal val invocableType = TypeVariableName("Invocable")

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
internal val editedChannelPostUpdate = EditedChannelPostUpdate::class.asTypeName()
internal val inlineQueryUpdateClass = InlineQueryUpdate::class.asTypeName()
internal val chosenInlineResultUpdateClass = ChosenInlineResultUpdate::class.asTypeName()
internal val shippingQueryUpdateClass = ShippingQueryUpdate::class.asTypeName()
internal val preCheckoutQueryUpdateClass = PreCheckoutQueryUpdate::class.asTypeName()
internal val pollUpdateClass = PollUpdate::class.asTypeName()
internal val pollAnswerUpdateClass = PollAnswerUpdate::class.asTypeName()
internal val myChatMemberUpdateClass = MyChatMemberUpdate::class.asTypeName()
internal val chatMemberUpdateClass = ChatMemberUpdate::class.asTypeName()
internal val chatJoinRequestUpdateClass = ChatJoinRequestUpdate::class.asTypeName()

internal val chainLinkClass = ChainLink::class.asTypeName()

internal fun List<KSValueArgument>.parseAsCommandHandler() = Triple(
    get(0).value.cast<List<String>>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
    last().value.cast<List<KSType>>().map { UpdateType.valueOf(it.declaration.toString()) },
)

internal fun List<KSValueArgument>.parseAsInputHandler() = Pair(
    get(0).value.cast<List<String>>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
)

internal fun List<KSValueArgument>.parseAsRegexHandler() = Pair(
    get(0).value.cast<String>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
)

internal fun List<KSValueArgument>.parseAsUpdateHandler() = first().value.cast<List<KSType>>().map {
    UpdateType.valueOf(it.declaration.toString())
}

internal fun List<KSValueArgument>.parseAsLinkOptions() = Pair(
    get(0).value.cast<Boolean>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
)

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <R> Any?.cast(): R = this as R

internal fun Pair<Long, Long>.toRateLimits() = RateLimits(first, second)

internal fun String.withPostfix(postfix: String?): String = postfix?.let { "${this}_$postfix" } ?: this
internal fun String.escape() = replace(".", "_")

internal fun <T : Annotation> Resolver.getAnnotatedFnSymbols(
    clazz: KClass<T>,
    targetPackage: String? = null,
): Sequence<KSFunctionDeclaration> =
    if (targetPackage == null)
        getSymbolsWithAnnotation(clazz.qualifiedName!!).filterIsInstance<KSFunctionDeclaration>()
    else getSymbolsWithAnnotation(clazz.qualifiedName!!).filter {
        it is KSFunctionDeclaration && it.packageName.asString().startsWith(targetPackage)
    }.cast()

internal fun <T : Annotation> Resolver.getAnnotatedClassSymbols(clazz: KClass<T>, targetPackage: String? = null) =
    if (targetPackage == null) getSymbolsWithAnnotation(clazz.qualifiedName!!).filterIsInstance<KSClassDeclaration>()
    else getSymbolsWithAnnotation(clazz.qualifiedName!!).filter {
        it is KSClassDeclaration && it.packageName.asString().startsWith(targetPackage)
    }.cast()

internal fun FileBuilder.addMap(
    name: String,
    type: ParameterizedTypeName,
    symbols: Sequence<KSFunctionDeclaration>,
    tailBlock: CodeBlock? = null,
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
                if (symbols.iterator().hasNext()) symbols.forEach {
                    iterableAction(it)
                }
                tailBlock?.also { add(it) }
                add(")\n")
            }.build(),
        )
    }.build(),
)
