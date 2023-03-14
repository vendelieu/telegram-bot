@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotShortDescription
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetMyShortDescriptionAction(languageCode: String? = null) : SimpleAction<BotShortDescription>, ActionState() {
    override val method: TgMethod = TgMethod("getMyDescription")
    override val returnType = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun getMyShortDescription(languageCode: String? = null) = GetMyShortDescriptionAction(languageCode)
