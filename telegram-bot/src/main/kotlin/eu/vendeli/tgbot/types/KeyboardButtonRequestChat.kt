package eu.vendeli.tgbot.types

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
