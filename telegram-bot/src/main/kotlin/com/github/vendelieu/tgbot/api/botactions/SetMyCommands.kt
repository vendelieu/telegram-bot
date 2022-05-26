package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.BotCommand
import com.github.vendelieu.tgbot.types.BotCommandScope
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetMyCommandsAction(
    commands: List<BotCommand>,
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("setMyCommands")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["commands"] = commands
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, vararg command: BotCommand) =
    SetMyCommandsAction(listOf(*command), scope, languageCode)
