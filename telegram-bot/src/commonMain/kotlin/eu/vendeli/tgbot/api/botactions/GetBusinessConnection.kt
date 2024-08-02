@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.business.BusinessConnection
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetBusinessConnectionAction(
    businessConnectionId: String,
) : SimpleAction<BusinessConnection>() {
    override val method = TgMethod("getBusinessConnection")
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
    }
}

/**
 * Use this method to get information about the connection of the bot with a business account. Returns a BusinessConnection object on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getbusinessconnection)
 * @param businessConnectionId Unique identifier of the business connection
 * @returns [BusinessConnection]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getBusinessConnection(businessConnectionId: String) = GetBusinessConnectionAction(businessConnectionId)
