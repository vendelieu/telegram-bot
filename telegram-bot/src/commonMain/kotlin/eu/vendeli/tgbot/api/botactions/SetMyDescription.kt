@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetMyDescriptionAction(
    description: String? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    override val method = "setMyDescription"
    override val returnType = getReturnType()

    init {
        if (description != null) parameters["description"] = description.toJsonElement()
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmydescription)
 * @param description New bot description; 0-512 characters. Pass an empty string to remove the dedicated description for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the description will be applied to all users for whose language there is no dedicated description.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun setMyDescription(
    description: String? = null,
    languageCode: String? = null,
) = SetMyDescriptionAction(description, languageCode)
