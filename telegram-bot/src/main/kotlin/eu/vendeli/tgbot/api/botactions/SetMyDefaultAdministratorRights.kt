@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetMyDefaultAdministratorRightsAction(
    rights: ChatAdministratorRights? = null,
    forChannel: Boolean? = null,
) : SimpleAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setMyDefaultAdministratorRights")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        if (rights != null) parameters["rights"] = rights
        if (forChannel != null) parameters["for_channel"] = forChannel
    }
}

fun setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null, forChannel: Boolean? = null) =
    SetMyDefaultAdministratorRightsAction(rights, forChannel)
