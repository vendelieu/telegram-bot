@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.gift.StarAmount
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetBusinessAccountStarBalanceAction(
    businessConnectionId: String,
) : SimpleAction<StarAmount>() {
    @TgAPI.Name("getBusinessAccountStarBalance")
    override val method = "getBusinessAccountStarBalance"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
    }
}

/**
 * Returns the amount of Telegram Stars owned by a managed business account. Requires the can_view_gifts_and_stars business bot right. Returns StarAmount on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getbusinessaccountstarbalance)
 * @param businessConnectionId Unique identifier of the business connection
 * @returns [StarAmount]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun getBusinessAccountStarBalance(
    businessConnectionId: String,
) = GetBusinessAccountStarBalanceAction(businessConnectionId)
