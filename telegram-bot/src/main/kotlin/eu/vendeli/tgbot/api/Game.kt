@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GameOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendGameAction(
    gameShortName: String,
) : Action<Message>,
    ActionState(),
    OptionsFeature<SendGameAction, GameOptions>,
    MarkupFeature<SendGameAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendGame")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendGameAction, GameOptions>.options: GameOptions
        get() = GameOptions()

    init {
        parameters["game_short_name"] = gameShortName
    }
}

fun sendGame(gameShortName: String) = game(gameShortName)
fun game(gameShortName: String) = SendGameAction(gameShortName)
