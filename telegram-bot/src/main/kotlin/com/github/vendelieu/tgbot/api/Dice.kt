package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.DiceOptions

class SendDiceAction(emoji: String? = null) :
    Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendDiceAction, DiceOptions>,
    MarkupFeature<SendDiceAction> {
    override val method: TgMethod = TgMethod("sendDice")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
    override var options = DiceOptions()

    init {
        if (emoji != null) parameters["emoji"] = emoji
    }
}

fun dice(emoji: String? = null) = SendDiceAction(emoji)
