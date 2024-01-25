@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GameOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendGameAction(
    gameShortName: String,
) : Action<Message>(),
    OptionsFeature<SendGameAction, GameOptions>,
    MarkupFeature<SendGameAction> {
    override val method = TgMethod("sendGame")
    override val returnType = getReturnType()
    override val options = GameOptions()

    init {
        parameters["game_short_name"] = gameShortName.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun sendGame(gameShortName: String) = game(gameShortName)

@Suppress("NOTHING_TO_INLINE")
inline fun game(gameShortName: String) = SendGameAction(gameShortName)
