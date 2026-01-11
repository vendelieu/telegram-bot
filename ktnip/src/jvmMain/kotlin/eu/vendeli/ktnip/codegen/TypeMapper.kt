package eu.vendeli.ktnip.codegen

import com.squareup.kotlinpoet.TypeName
import eu.vendeli.ktnip.utils.TypeConstants
import eu.vendeli.tgbot.types.component.*
import kotlin.reflect.KClass

/**
 * Maps TypeNames to their corresponding update type classes and simple names.
 * Centralizes type mapping logic that was scattered in InvocationLambdaBuilder.
 */
object TypeMapper {
    /**
     * Map of TypeName to update type KClass for type checking.
     */
    private val updateTypeMap: Map<TypeName, KClass<out ProcessedUpdate>> = mapOf(
        TypeConstants.messageUpdClass to MessageUpdate::class,
        TypeConstants.callbackQueryUpdateClass to CallbackQueryUpdate::class,
        TypeConstants.editedMessageUpdateClass to EditedMessageUpdate::class,
        TypeConstants.channelPostUpdateClass to ChannelPostUpdate::class,
        TypeConstants.editedChannelPostUpdateClass to EditedChannelPostUpdate::class,
        TypeConstants.messageReactionUpdateClass to MessageReactionUpdate::class,
        TypeConstants.messageReactionCountUpdateClass to MessageReactionCountUpdate::class,
        TypeConstants.inlineQueryUpdateClass to InlineQueryUpdate::class,
        TypeConstants.chosenInlineResultUpdateClass to ChosenInlineResultUpdate::class,
        TypeConstants.shippingQueryUpdateClass to ShippingQueryUpdate::class,
        TypeConstants.preCheckoutQueryUpdateClass to PreCheckoutQueryUpdate::class,
        TypeConstants.pollUpdateClass to PollUpdate::class,
        TypeConstants.pollAnswerUpdateClass to PollAnswerUpdate::class,
        TypeConstants.myChatMemberUpdateClass to MyChatMemberUpdate::class,
        TypeConstants.chatMemberUpdateClass to ChatMemberUpdate::class,
        TypeConstants.chatJoinRequestUpdateClass to ChatJoinRequestUpdate::class,
        TypeConstants.chatBoostUpdateClass to ChatBoostUpdate::class,
        TypeConstants.removedChatBoostUpdateClass to RemovedChatBoostUpdate::class,
        TypeConstants.businessConnectionUpdateClass to BusinessConnectionUpdate::class,
        TypeConstants.businessMessageUpdateClass to BusinessMessageUpdate::class,
        TypeConstants.editedBusinessMessageClass to EditedBusinessMessageUpdate::class,
        TypeConstants.deletedBusinessMessagesClass to DeletedBusinessMessagesUpdate::class,
        TypeConstants.purchasedPaidMediaUpdateClass to PurchasedPaidMediaUpdate::class,
    )

    /**
     * Checks if the given TypeName represents a typed update.
     */
    fun isTypedUpdate(typeName: TypeName): Boolean =
        updateTypeMap.containsKey(typeName)

    /**
     * Gets the simple name of the update type for code generation.
     */
    fun getUpdateTypeSimpleName(typeName: TypeName): String? =
        updateTypeMap[typeName]?.simpleName

    /**
     * Gets all supported typed update TypeNames.
     */
    fun getAllTypedUpdateTypes(): Set<TypeName> =
        updateTypeMap.keys
}
