package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import kotlinx.serialization.Serializable

@Serializable
data class KeyboardButtonRequestChat(
    val requestId: Int,
    val chatIsChannel: Boolean,
    val chatIsForum: Boolean? = null,
    val chatHasUserName: Boolean? = null,
    val chatIsCreated: Boolean? = null,
    val userAdministratorRights: ChatAdministratorRights? = null,
    val botAdministratorRights: ChatAdministratorRights? = null,
    val botIsMember: Boolean? = null,
)
