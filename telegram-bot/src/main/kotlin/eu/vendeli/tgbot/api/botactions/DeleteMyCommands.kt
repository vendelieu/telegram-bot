@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod

class DeleteMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteMyCommands")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun deleteMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    DeleteMyCommandsAction(scope, languageCode)
