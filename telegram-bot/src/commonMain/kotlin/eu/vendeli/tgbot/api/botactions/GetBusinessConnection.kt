@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.business.BusinessConnection
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetBusinessConnectionAction(businessConnectionId: String) :
    SimpleAction<BusinessConnection>() {
    override val method = TgMethod("getBusinessConnection")
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getBusinessConnection(businessConnectionId: String) = GetBusinessConnectionAction(businessConnectionId)
