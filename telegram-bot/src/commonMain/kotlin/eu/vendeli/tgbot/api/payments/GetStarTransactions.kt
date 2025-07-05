@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.payments

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.stars.StarTransactions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetStarTransactionsAction(
    offset: Int? = null,
    limit: Int? = null,
) : SimpleAction<StarTransactions>() {
    @TgAPI.Name("getStarTransactions")
    override val method = "getStarTransactions"
    override val returnType = getReturnType()

    init {
        if (offset != null) parameters["offset"] = offset.toJsonElement()
        if (limit != null) parameters["limit"] = limit.toJsonElement()
    }
}

/**
 * Returns the bot's Telegram Star transactions in chronological order. On success, returns a StarTransactions object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getstartransactions)
 * @param offset Number of transactions to skip in the response
 * @param limit The maximum number of transactions to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 * @returns [StarTransactions]
 */
@TgAPI
inline fun getStarTransactions(offset: Int? = null, limit: Int? = null) = GetStarTransactionsAction(offset, limit)
