@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotName
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyNameAction(languageCode: String? = null) : SimpleAction<BotName>() {
    override val method = TgMethod("getMyName")
    override val returnType = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getMyName(languageCode: String? = null) = GetMyNameAction(languageCode)
