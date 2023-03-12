@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("deleteMyCommands")
    override val returnType = getReturnType()

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun deleteMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    DeleteMyCommandsAction(scope, languageCode)
