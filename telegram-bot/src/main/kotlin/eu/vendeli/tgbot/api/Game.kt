@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GameOptions

class SendGameAction(
    gameShortName: String,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendGameAction, GameOptions>,
    MarkupFeature<SendGameAction> {
    override val method: TgMethod = TgMethod("sendGame")
    override var options = GameOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["game_short_name"] = gameShortName
    }
}

fun game(gameShortName: String) = SendGameAction(gameShortName)
