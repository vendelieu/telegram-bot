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

@Suppress("NOTHING_TO_INLINE")
inline fun getMyShortDescription(languageCode: String? = null) = GetMyShortDescriptionAction(languageCode)
