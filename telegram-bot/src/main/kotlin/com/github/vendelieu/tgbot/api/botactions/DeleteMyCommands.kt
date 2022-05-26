package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.BotCommandScope
import com.github.vendelieu.tgbot.types.internal.TgMethod

class DeleteMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteMyCommands")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun deleteMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    DeleteMyCommandsAction(scope, languageCode)
