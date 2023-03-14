@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetMyDefaultAdministratorRightsAction(forChannel: Boolean? = null) :
    SimpleAction<ChatAdministratorRights>, ActionState() {
    override val TgAction<ChatAdministratorRights>.method: TgMethod
        get() = TgMethod("getMyDefaultAdministratorRights")
    override val TgAction<ChatAdministratorRights>.returnType: Class<ChatAdministratorRights>
        get() = getReturnType()

    init {
        if (forChannel != null) parameters["for_channel"] = forChannel
    }
}

fun getMyDefaultAdministratorRights(forChannel: Boolean? = null) = GetMyDefaultAdministratorRightsAction(forChannel)
