@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.business.BusinessConnection
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetBusinessConnectionAction(
    businessConnectionId: String,
) : SimpleAction<BusinessConnection>() {
    @TgAPI.Name("getBusinessConnection")
    override val method = "getBusinessConnection"
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
@TgAPI
inline fun getBusinessConnection(businessConnectionId: String) = GetBusinessConnectionAction(businessConnectionId)
