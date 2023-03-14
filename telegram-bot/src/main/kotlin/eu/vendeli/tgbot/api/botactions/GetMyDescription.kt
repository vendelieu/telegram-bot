@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.bot.BotDescription
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetMyDescriptionAction(languageCode: String? = null) : SimpleAction<BotDescription>, ActionState() {
    override val TgAction<BotDescription>.method: TgMethod
        get() = TgMethod("getMyDescription")
    override val TgAction<BotDescription>.returnType: Class<BotDescription>
        get() = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun getMyDescription(languageCode: String? = null) = GetMyDescriptionAction(languageCode)
