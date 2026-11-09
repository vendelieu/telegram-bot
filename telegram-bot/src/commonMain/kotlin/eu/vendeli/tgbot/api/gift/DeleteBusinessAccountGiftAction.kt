@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class DeleteBusinessAccountGiftAction(
    businessConnectionId: String,
    ownedGiftId: String,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("deleteBusinessAccountGift")
    override val method = "deleteBusinessAccountGift"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["owned_gift_id"] = ownedGiftId.toJsonElement()
    }
}

/**
 * Deletes a regular gift owned by a managed business account without receiving Telegram Stars for it. Requires the can_convert_gifts_to_stars business bot right. The gift can be deleted only if it isn't limited (the field total_count of the Gift is omitted) and wasn't refunded. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletebusinessaccountgift)
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be deleted
 * @returns [Boolean]
 */
@TgAPI
inline fun deleteBusinessAccountGift(
    businessConnectionId: String,
    ownedGiftId: String,
) = DeleteBusinessAccountGiftAction(businessConnectionId, ownedGiftId)
