@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class TransferBusinessAccountStarsAction(
    businessConnectionId: String,
    starCount: Int,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("transferBusinessAccountStars")
    override val method = "transferBusinessAccountStars"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["star_count"] = starCount.toJsonElement()
    }
}

/**
 * Transfers Telegram Stars from the business account balance to the bot's balance. Requires the can_transfer_stars business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#transferbusinessaccountstars)
 * @param businessConnectionId Unique identifier of the business connection
 * @param starCount Number of Telegram Stars to transfer; 1-10000
 * @returns [Boolean]
 */
@TgAPI
inline fun transferBusinessAccountStars(
    businessConnectionId: String,
    starCount: Int,
) = TransferBusinessAccountStarsAction(businessConnectionId, starCount)
