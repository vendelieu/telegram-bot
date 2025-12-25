package eu.vendeli.ksp.utils

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.ClassDataImpl
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import eu.vendeli.tgbot.interfaces.ctx.UserData
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.ChainingStrategy
import eu.vendeli.tgbot.types.chain.Link
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.*
import eu.vendeli.tgbot.utils.common.fqName

/**
 * Type name constants used throughout KSP code generation.
 * Centralizes all type references to avoid duplication.
 */
object TypeConstants {
    // Primitive types
    val intPrimitiveType: TypeName = TypeVariableName("int")
    val longPrimitiveType: TypeName = TypeVariableName("long")
    val shortPrimitiveType: TypeName = TypeVariableName("short")
    val floatPrimitiveType: TypeName = TypeVariableName("float")
    val doublePrimitiveType: TypeName = TypeVariableName("double")

    // Core bot types
    val userClass: TypeName = User::class.asTypeName()
    val chatClass: TypeName = Chat::class.asTypeName()
    val botClass: TypeName = TelegramBot::class.asTypeName()
    val processingCtx: TypeName = AdditionalContext::class.asTypeName()
    val idLongClass: TypeName = IdLong::class.asTypeName()

    // Update types
    val updateClass: TypeName = ProcessedUpdate::class.asTypeName()
    val messageUpdClass: TypeName = MessageUpdate::class.asTypeName()
    val callbackQueryUpdateClass: TypeName = CallbackQueryUpdate::class.asTypeName()
    val editedMessageUpdateClass: TypeName = EditedMessageUpdate::class.asTypeName()
    val channelPostUpdateClass: TypeName = ChannelPostUpdate::class.asTypeName()
    val editedChannelPostUpdateClass: TypeName = EditedChannelPostUpdate::class.asTypeName()
    val messageReactionUpdateClass: TypeName = MessageReactionUpdate::class.asTypeName()
    val messageReactionCountUpdateClass: TypeName = MessageReactionCountUpdate::class.asTypeName()
    val inlineQueryUpdateClass: TypeName = InlineQueryUpdate::class.asTypeName()
    val chosenInlineResultUpdateClass: TypeName = ChosenInlineResultUpdate::class.asTypeName()
    val shippingQueryUpdateClass: TypeName = ShippingQueryUpdate::class.asTypeName()
    val preCheckoutQueryUpdateClass: TypeName = PreCheckoutQueryUpdate::class.asTypeName()
    val pollUpdateClass: TypeName = PollUpdate::class.asTypeName()
    val pollAnswerUpdateClass: TypeName = PollAnswerUpdate::class.asTypeName()
    val myChatMemberUpdateClass: TypeName = MyChatMemberUpdate::class.asTypeName()
    val chatMemberUpdateClass: TypeName = ChatMemberUpdate::class.asTypeName()
    val chatJoinRequestUpdateClass: TypeName = ChatJoinRequestUpdate::class.asTypeName()
    val chatBoostUpdateClass: TypeName = ChatBoostUpdate::class.asTypeName()
    val removedChatBoostUpdateClass: TypeName = RemovedChatBoostUpdate::class.asTypeName()
    val businessConnectionUpdateClass: TypeName = BusinessConnectionUpdate::class.asTypeName()
    val businessMessageUpdateClass: TypeName = BusinessMessageUpdate::class.asTypeName()
    val editedBusinessMessageClass: TypeName = EditedBusinessMessageUpdate::class.asTypeName()
    val deletedBusinessMessagesClass: TypeName = DeletedBusinessMessagesUpdate::class.asTypeName()
    val purchasedPaidMediaUpdateClass: TypeName = PurchasedPaidMediaUpdate::class.asTypeName()

    // Context types
    val commonMatcherClass: TypeName = CommonMatcher::class.asTypeName()
    val userDataCtx: TypeName = UserData::class.asTypeName().parameterizedBy(STAR)
    val userDataCtxDef: TypeName = UserDataMapImpl::class.asTypeName()
    val classDataCtx: TypeName = ClassData::class.asTypeName().parameterizedBy(STAR)
    val classDataCtxDef: TypeName = ClassDataImpl::class.asTypeName()

    // Chain types
    val ChainingStrategyDefault: TypeName = ChainingStrategy.Default::class.asTypeName()

    // Fully qualified names
    val linkQName: String = Link::class.fqName
    val autowiringFQName: String = Autowiring::class.fqName

    val messageList = listOf(UpdateType.MESSAGE)
}
