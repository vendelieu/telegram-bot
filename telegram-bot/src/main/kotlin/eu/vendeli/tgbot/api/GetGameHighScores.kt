@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.types.game.GameHighScore
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class GetGameHighScoresAction : InlinableAction<List<GameHighScore>> {
    override val method = TgMethod("getGameHighScores")
    override val returnType = getReturnType()
    override val wrappedDataType = getInnerType()

    constructor(userId: Long, messageId: Long) {
        parameters["user_id"] = userId
        parameters["message_id"] = messageId
    }

    constructor(userId: Long) {
        parameters["user_id"] = userId
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(userId: Long, messageId: Long) = GetGameHighScoresAction(userId, messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(userId: Long) = GetGameHighScoresAction(userId)
