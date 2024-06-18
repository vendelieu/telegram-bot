@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.stars.StarTransactions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetStarTransactionsAction(offset: Int? = null, limit: Int? = null) : SimpleAction<StarTransactions>() {
    override val method = TgMethod("getStarTransactions")
    override val returnType = getReturnType()

    init {
        if (offset != null) parameters["offset"] = offset.toJsonElement()
        if (limit != null) parameters["limit"] = limit.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getStarTransactions(offset: Int? = null, limit: Int? = null) = GetStarTransactionsAction(offset, limit)
