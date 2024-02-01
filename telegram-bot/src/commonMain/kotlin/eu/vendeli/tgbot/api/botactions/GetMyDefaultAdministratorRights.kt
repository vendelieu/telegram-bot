@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyDefaultAdministratorRightsAction(forChannel: Boolean? = null) :
    SimpleAction<ChatAdministratorRights>() {
    override val method = TgMethod("getMyDefaultAdministratorRights")
    override val returnType = getReturnType()

    init {
        if (forChannel != null) parameters["for_channel"] = forChannel.toJsonElement()
    }
}

inline fun getMyDefaultAdministratorRights(forChannel: Boolean? = null) =
    GetMyDefaultAdministratorRightsAction(forChannel)
