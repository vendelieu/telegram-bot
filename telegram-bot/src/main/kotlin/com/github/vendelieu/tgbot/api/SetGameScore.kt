package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.SetGameScoreOptions

class SetGameScoreAction : Action<Message>, OptionAble, OptionsFeature<SetGameScoreAction, SetGameScoreOptions> {
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
