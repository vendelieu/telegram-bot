package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.Feature
import com.github.vendelieu.tgbot.types.GameHighScore
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetGameHighScoresAction : Action<GameHighScore> {
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
