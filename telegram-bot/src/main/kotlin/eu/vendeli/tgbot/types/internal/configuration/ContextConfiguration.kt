package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.core.ChatDataMapImpl
import eu.vendeli.tgbot.core.UserDataMapImpl
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
    internal var _chatData: ChatData? = null,
    private var _userData: UserData? = null
) {
    var userData: UserData
        get() = if (_userData != null) _userData!! else UserDataMapImpl.also {
            _userData = it
        }
        set(value) {
            if (_userData == null) _userData = value
        }

    var chatData: ChatData
        get() = if (_chatData != null) _chatData!! else ChatDataMapImpl.also {
            _chatData = it
        }
        set(value) {
            if (_chatData == null) _chatData = value
        }
}
