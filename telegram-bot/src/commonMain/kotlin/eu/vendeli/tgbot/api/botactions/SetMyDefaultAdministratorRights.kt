@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetMyDefaultAdministratorRightsAction(
    rights: ChatAdministratorRights? = null,
    forChannel: Boolean? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setMyDefaultAdministratorRights")
    override val returnType = getReturnType()

    init {
        if (rights != null) parameters["rights"] = rights.encodeWith(ChatAdministratorRights.serializer())
        if (forChannel != null) parameters["for_channel"] = forChannel.toJsonElement()
    }
}

inline fun setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null, forChannel: Boolean? = null) =
    SetMyDefaultAdministratorRightsAction(rights, forChannel)
