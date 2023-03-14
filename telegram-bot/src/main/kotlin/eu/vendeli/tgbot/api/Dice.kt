@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DiceOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendDiceAction(emoji: String? = null) :
    Action<Message>,
    ActionState(),
    OptionsFeature<SendDiceAction, DiceOptions>,
    MarkupFeature<SendDiceAction> {
    override val method: TgMethod = TgMethod("sendDice")
    override val returnType = getReturnType()
    override val OptionsFeature<SendDiceAction, DiceOptions>.options: DiceOptions
        get() = DiceOptions()

    init {
        if (emoji != null) parameters["emoji"] = emoji
    }
}

fun dice(emoji: String? = null) = SendDiceAction(emoji)
