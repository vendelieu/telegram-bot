@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.bot.BotShortDescription
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetMyShortDescriptionAction(languageCode: String? = null) : SimpleAction<BotShortDescription>, ActionState() {
    override val TgAction<BotShortDescription>.method: TgMethod
        get() = TgMethod("getMyShortDescription")
    override val TgAction<BotShortDescription>.returnType: Class<BotShortDescription>
        get() = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun getMyShortDescription(languageCode: String? = null) = GetMyShortDescriptionAction(languageCode)
