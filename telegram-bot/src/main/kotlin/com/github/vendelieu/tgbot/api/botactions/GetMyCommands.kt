package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.*
import com.github.vendelieu.tgbot.types.BotCommand
import com.github.vendelieu.tgbot.types.BotCommandScope
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<List<BotCommand>>, MultiResponseOf<BotCommand> {
    override val method: TgMethod = TgMethod("getMyCommands")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T> = getInnerType() as Class<T>

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun getMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    GetMyCommandsAction(scope, languageCode)
