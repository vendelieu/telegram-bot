package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.ChatAdministratorRights
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetMyDefaultAdministratorRightsAction(forChannel: Boolean? = null) :
    SimpleAction<ChatAdministratorRights> {
    override val method: TgMethod = TgMethod("getMyDefaultAdministratorRights")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        if (forChannel != null) parameters["for_channel"] = forChannel
    }
}

fun getMyDefaultAdministratorRights(forChannel: Boolean? = null) = GetMyDefaultAdministratorRightsAction(forChannel)
