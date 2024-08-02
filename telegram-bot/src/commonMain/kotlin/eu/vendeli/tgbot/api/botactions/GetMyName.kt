@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.BotName
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyNameAction(
    languageCode: String? = null,
) : SimpleAction<BotName>() {
    override val method = TgMethod("getMyName")
    override val returnType = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to get the current bot name for the given user language. Returns BotName on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getmyname)
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 * @returns [BotName]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getMyName(languageCode: String? = null) = GetMyNameAction(languageCode)
