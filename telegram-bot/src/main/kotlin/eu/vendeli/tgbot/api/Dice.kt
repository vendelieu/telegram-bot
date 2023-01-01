@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DiceOptions

class SendDiceAction(emoji: String? = null) :
    Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendDiceAction, DiceOptions>,
    MarkupFeature<SendDiceAction> {
    override val method: TgMethod = TgMethod("sendDice")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override var options = DiceOptions()

    init {
        if (emoji != null) parameters["emoji"] = emoji
    }
}

fun dice(emoji: String? = null) = SendDiceAction(emoji)
