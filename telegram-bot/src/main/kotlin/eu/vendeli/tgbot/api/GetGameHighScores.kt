@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.game.GameHighScore
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class GetGameHighScoresAction : InlinableAction<List<GameHighScore>> {
    override val method = TgMethod("getGameHighScores")
    override val returnType = getReturnType()
    override val wrappedDataType = getInnerType()

    constructor(user: Identifier<*>, messageId: Long) {
        parameters["user_id"] = user.get
        parameters["message_id"] = messageId
    }

    constructor(user: Identifier<*>) {
        parameters["user_id"] = user.get
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(userId: Long, messageId: Long) =
    GetGameHighScoresAction(Identifier.from(userId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(userId: Long) = GetGameHighScoresAction(Identifier.from(userId))

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(user: User, messageId: Long) = getGameHighScores(user.id, messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(user: User) = getGameHighScores(user.id)

