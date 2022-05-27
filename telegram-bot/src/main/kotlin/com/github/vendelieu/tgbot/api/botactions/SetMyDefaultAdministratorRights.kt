package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.ChatAdministratorRights
import com.github.vendelieu.tgbot.types.internal.TgMethod

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
