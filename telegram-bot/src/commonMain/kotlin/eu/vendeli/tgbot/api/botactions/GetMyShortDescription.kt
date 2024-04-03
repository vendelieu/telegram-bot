@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotShortDescription
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyShortDescriptionAction(languageCode: String? = null) : SimpleAction<BotShortDescription>() {
    override val method = TgMethod("getMyShortDescription")
    override val returnType = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to get the current bot short description for the given user language. Returns BotShortDescription on success.
 *
 * Api reference: https://core.telegram.org/bots/api#getmyshortdescription
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 * @returns [BotShortDescription]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getMyShortDescription(languageCode: String? = null) = GetMyShortDescriptionAction(languageCode)
