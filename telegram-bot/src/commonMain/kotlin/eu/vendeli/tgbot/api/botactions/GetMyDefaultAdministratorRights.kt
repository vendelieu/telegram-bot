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

/**
 * Use this method to get the current default administrator rights of the bot. Returns ChatAdministratorRights on success.
 *
 * Api reference: https://core.telegram.org/bots/api#getmydefaultadministratorrights
 * @param forChannels Pass True to get default administrator rights of the bot in channels. Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
 * @returns [ChatAdministratorRights]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getMyDefaultAdministratorRights(forChannel: Boolean? = null) =
    GetMyDefaultAdministratorRightsAction(forChannel)
