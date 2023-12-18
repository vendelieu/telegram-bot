package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.implementations.ChatDataMapImpl
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.interfaces.ChatData
import eu.vendeli.tgbot.interfaces.UserData

/**
 * A class containing configurations related to
 * [bot context](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context).
 *
 * @property userData Parameter to manage UserData, can be set once per instance.
 * @property chatData Parameter to manage ChatData, can be set once per instance.
 */
@Suppress("VariableNaming", "ConstructorParameterNaming")
data class ContextConfiguration(
    internal var userData: UserData = UserDataMapImpl,
    internal var chatData: ChatData = ChatDataMapImpl,
)
