@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.bot.BotName
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetMyNameAction(languageCode: String? = null) : SimpleAction<BotName>, ActionState() {
    override val TgAction<BotName>.method: TgMethod
        get() = TgMethod("getMyName")
    override val TgAction<BotName>.returnType: Class<BotName>
        get() = getReturnType()

    init {
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun getMyName(languageCode: String? = null) = GetMyNameAction(languageCode)
