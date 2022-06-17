package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod

class GetMyDefaultAdministratorRightsAction(forChannel: Boolean? = null) :
    SimpleAction<ChatAdministratorRights> {
    override val method: TgMethod = TgMethod("getMyDefaultAdministratorRights")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        if (forChannel != null) parameters["for_channel"] = forChannel
    }
}

fun getMyDefaultAdministratorRights(forChannel: Boolean? = null) = GetMyDefaultAdministratorRightsAction(forChannel)
