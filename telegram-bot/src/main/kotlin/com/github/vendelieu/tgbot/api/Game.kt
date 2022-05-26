package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.GameOptions

class SendGameAction(
    gameShortName: String,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendGameAction, GameOptions>,
    MarkupFeature<SendGameAction> {
    override val method: TgMethod = TgMethod("sendGame")
    override var options = GameOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["game_short_name"] = gameShortName
    }
}

fun game(gameShortName: String) = SendGameAction(gameShortName)
