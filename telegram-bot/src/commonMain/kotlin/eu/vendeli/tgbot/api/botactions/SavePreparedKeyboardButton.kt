@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.keyboard.KeyboardButton
import eu.vendeli.tgbot.types.keyboard.PreparedKeyboardButton
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SavePreparedKeyboardButtonAction(
    userId: Long,
    button: KeyboardButton,
) : SimpleAction<PreparedKeyboardButton>() {
    @TgAPI.Name("savePreparedKeyboardButton")
    override val method = "savePreparedKeyboardButton"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["button"] = button.encodeWith(KeyboardButton.serializer())
    }
}

/**
 * Stores a keyboard button that can be used by a user within a Mini App. Returns a PreparedKeyboardButton object.
 *
 * [Api reference](https://core.telegram.org/bots/api#savepreparedkeyboardbutton)
 * @param userId Unique identifier of the target user that can use the button
 * @param button A JSON-serialized object describing the button to be saved. The button must be of the type request_users, request_chat, or request_managed_bot
 * @returns [PreparedKeyboardButton]
 */
@TgAPI
inline fun savePreparedKeyboardButton(
    userId: Long,
    button: KeyboardButton,
) = SavePreparedKeyboardButtonAction(userId, button)
