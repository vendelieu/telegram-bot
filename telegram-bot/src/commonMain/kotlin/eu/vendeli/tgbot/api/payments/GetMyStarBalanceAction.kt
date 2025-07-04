package eu.vendeli.tgbot.api.payments

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.gift.StarAmount
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetMyStarBalanceAction : SimpleAction<StarAmount>() {
    @TgAPI.Name("getMyStarBalance")
    override val method = "getMyStarBalance"
    override val returnType = getReturnType()
}

/**
 * A method to get the current Telegram Stars balance of the bot. Requires no parameters. On success, returns a StarAmount object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getmystarbalance)
 *
 * @returns [StarAmount]
 */
@TgAPI
inline fun getMyStarBalance() = GetMyStarBalanceAction()
