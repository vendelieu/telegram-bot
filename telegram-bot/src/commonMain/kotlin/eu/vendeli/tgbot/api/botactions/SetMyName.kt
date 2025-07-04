@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetMyNameAction(
    name: String? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setMyName")
    override val method = "setMyName"
    override val returnType = getReturnType()

    init {
        if (name != null) parameters["name"] = name.toJsonElement()
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to change the bot's name. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmyname)
 * @param name New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose language there is no dedicated name.
 * @returns [Boolean]
 */
@TgAPI
inline fun setMyName(name: String? = null, languageCode: String? = null) = SetMyNameAction(name, languageCode)
