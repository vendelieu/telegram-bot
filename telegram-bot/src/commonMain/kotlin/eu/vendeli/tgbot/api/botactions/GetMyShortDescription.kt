@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.BotShortDescription
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class GetMyShortDescriptionAction(
    languageCode: String? = null,
) : SimpleAction<BotShortDescription>() {
    override val method = "getMyShortDescription"
    override val returnType = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to get the current bot short description for the given user language. Returns BotShortDescription on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getmyshortdescription)
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 * @returns [BotShortDescription]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getMyShortDescription(languageCode: String? = null) = GetMyShortDescriptionAction(languageCode)
