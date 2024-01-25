@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DiceOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendDiceAction(emoji: String? = null) :
    Action<Message>(),
    OptionsFeature<SendDiceAction, DiceOptions>,
    MarkupFeature<SendDiceAction> {
    override val method = TgMethod("sendDice")
    override val returnType = getReturnType()
    override val options = DiceOptions()

    init {
        if (emoji != null) parameters["emoji"] = emoji.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun sendDice(emoji: String? = null) = dice(emoji)

@Suppress("NOTHING_TO_INLINE")
inline fun dice(emoji: String? = null) = SendDiceAction(emoji)
