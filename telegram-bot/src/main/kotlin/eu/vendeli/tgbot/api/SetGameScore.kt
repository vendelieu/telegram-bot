@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.SetGameScoreOptions
import eu.vendeli.tgbot.utils.getReturnType

class SetGameScoreAction :
    InlinableAction<Message>,
    OptionsFeature<SetGameScoreAction, SetGameScoreOptions> {
    override val method = TgMethod("setGameScore")
    override val returnType = getReturnType()
    override val OptionsFeature<SetGameScoreAction, SetGameScoreOptions>.options: SetGameScoreOptions
        get() = SetGameScoreOptions()

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
