@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotDescription
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyDescriptionAction(languageCode: String? = null) : SimpleAction<BotDescription>() {
    override val method = TgMethod("getMyDescription")
    override val returnType = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to get the current bot description for the given user language. Returns BotDescription on success.
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 * @returns [BotDescription]
 * Api reference: https://core.telegram.org/bots/api#getmydescription
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getMyDescription(languageCode: String? = null) = GetMyDescriptionAction(languageCode)
