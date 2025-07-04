@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class TransferGiftAction(
    businessConnectionId: String,
    ownedGiftId: String,
    newOwnerChatId: Long,
    starCount: Int? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("transferGift")
    override val method = "transferGift"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["owned_gift_id"] = ownedGiftId.toJsonElement()
        parameters["new_owner_chat_id"] = newOwnerChatId.toJsonElement()
        starCount?.let { parameters["star_count"] = it.toJsonElement() }
    }
}

/**
 * Transfers an owned unique gift to another user. Requires the can_transfer_and_upgrade_gifts business bot right. Requires can_transfer_stars business bot right if the transfer is paid. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#transfergift)
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be transferred
 * @param newOwnerChatId Unique identifier of the chat which will own the gift. The chat must be active in the last 24 hours.
 * @param starCount The amount of Telegram Stars that will be paid for the transfer from the business account balance. If positive, then the can_transfer_stars business bot right is required.
 * @returns [Boolean]
 */
@TgAPI
inline fun transferGift(
    businessConnectionId: String,
    ownedGiftId: String,
    newOwnerChatId: Long,
    starCount: Int? = null,
) = TransferGiftAction(businessConnectionId, ownedGiftId, newOwnerChatId, starCount)
