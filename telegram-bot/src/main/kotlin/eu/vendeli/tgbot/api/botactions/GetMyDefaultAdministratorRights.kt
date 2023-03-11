@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetMyDefaultAdministratorRightsAction(forChannel: Boolean? = null) :
    SimpleAction<ChatAdministratorRights>, ActionState() {
    override val method: TgMethod = TgMethod("getMyDefaultAdministratorRights")
    override val returnType = getReturnType()

    init {
        if (forChannel != null) parameters["for_channel"] = forChannel
    }
}

fun getMyDefaultAdministratorRights(forChannel: Boolean? = null) = GetMyDefaultAdministratorRightsAction(forChannel)
