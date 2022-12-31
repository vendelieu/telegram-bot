@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.SetGameScoreOptions

class SetGameScoreAction :
    Action<Message>,
    InlineMode<Message>,
    OptionAble,
    OptionsFeature<SetGameScoreAction, SetGameScoreOptions> {
    override val method: TgMethod = TgMethod("setGameScore")
    override var options = SetGameScoreOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(userId: Long, messageId: Long, score: Long) {
        parameters["user_id"] = userId
        parameters["score"] = score
        parameters["message_id"] = messageId
    }

    constructor(userId: Long, score: Long) {
        parameters["user_id"] = userId
        parameters["score"] = score
    }
}

fun setGameScore(userId: Long, messageId: Long, score: Long) = SetGameScoreAction(userId, messageId, score)
fun setGameScore(userId: Long, score: Long) = SetGameScoreAction(userId, score)
