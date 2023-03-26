package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.interfaces.UserData
import eu.vendeli.tgbot.interfaces.ChatData

/**
 * A class containing configurations related to
 * [bot context](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context).
 *
 * @property userData Parameter to manage UserData, can be set once per instance.
 * @property chatData Parameter to manage ChatData, can be set once per instance.
 */
@Suppress("PropertyName", "VariableNaming")
class ContextConfiguration {
    internal var _chatData: ChatData? = null
    private var _userData: UserData? = null

    var userData: UserData
        get() = _userData ?: throw NotImplementedError("Class to manage the User context is not set.")
        set(value) {
            if (_userData == null) _userData = value
        }

    var chatData: ChatData
        get() = _chatData ?: throw NotImplementedError("Class to manage the Chat context is not set.")
        set(value) {
            if (_chatData == null) _chatData = value
        }
}
