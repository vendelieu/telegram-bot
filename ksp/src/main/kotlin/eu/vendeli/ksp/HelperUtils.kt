package eu.vendeli.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
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

internal fun List<KSValueArgument>.parseToCommandHandlerArgs() = Triple(
    get(0).value.cast<List<String>>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
    last().value.cast<List<KSType>>().map { UpdateType.valueOf(it.declaration.toString()) },
)

internal fun List<KSValueArgument>.parseToInputHandlerArgs() = Pair(
    get(0).value.cast<List<String>>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
)

internal fun List<KSValueArgument>.parseToRegexHandlerArgs() = Pair(
    get(0).value.cast<String>(),
    get(1).value.cast<KSAnnotation>().arguments.let { it.first().value.cast<Long>() to it.last().value.cast<Long>() },
)

internal fun List<KSValueArgument>.parseToUpdateHandlerArgs() = first().value.cast<List<KSType>>().map {
    UpdateType.valueOf(it.declaration.toString())
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <R> Any?.cast(): R = this as R
