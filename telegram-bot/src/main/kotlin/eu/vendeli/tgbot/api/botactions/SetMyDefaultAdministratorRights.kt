package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod

class SetMyDefaultAdministratorRightsAction(
    rights: ChatAdministratorRights? = null,
    forChannel: Boolean? = null,
) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("setMyDefaultAdministratorRights")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        if (rights != null) parameters["rights"] = rights
        if (forChannel != null) parameters["for_channel"] = forChannel
    }
}

fun setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null, forChannel: Boolean? = null) =
    SetMyDefaultAdministratorRightsAction(rights, forChannel)
