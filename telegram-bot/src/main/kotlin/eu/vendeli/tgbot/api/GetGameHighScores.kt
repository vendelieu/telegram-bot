package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.types.GameHighScore
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod

class GetGameHighScoresAction : Action<GameHighScore>, InlineMode<Message> {
    override val method: TgMethod = TgMethod("getGameHighScores")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(userId: Long, messageId: Long) {
        parameters["user_id"] = userId
        parameters["message_id"] = messageId
    }

    constructor(userId: Long) {
        parameters["user_id"] = userId
    }
}

fun getGameHighScores(userId: Long, messageId: Long) = GetGameHighScoresAction(userId, messageId)
fun getGameHighScores(userId: Long) = GetGameHighScoresAction(userId)
