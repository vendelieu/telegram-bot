@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetMyShortDescriptionAction(
    shortDescription: String? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setMyShortDescription")
    override val method = "setMyShortDescription"
    override val returnType = getReturnType()

    init {
        if (shortDescription != null) parameters["short_description"] = shortDescription.toJsonElement()
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent together with the link when users share the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmyshortdescription)
 * @param shortDescription New short description for the bot; 0-120 characters. Pass an empty string to remove the dedicated short description for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the short description will be applied to all users for whose language there is no dedicated short description.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setMyShortDescription(
    description: String? = null,
    languageCode: String? = null,
) = SetMyShortDescriptionAction(description, languageCode)
