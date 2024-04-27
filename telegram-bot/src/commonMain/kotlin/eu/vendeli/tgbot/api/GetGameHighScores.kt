@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineActionExt
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.game.GameHighScore
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement

class GetGameHighScoresAction : Action<List<GameHighScore>>, InlineActionExt<List<GameHighScore>> {
    override val method = TgMethod("getGameHighScores")
    override val returnType = getReturnType()

    constructor(user: Identifier, messageId: Long) {
        parameters["user_id"] = user.encodeWith(DynamicLookupSerializer)
        parameters["message_id"] = messageId.toJsonElement()
    }

    constructor(user: Identifier) {
        parameters["user_id"] = user.encodeWith(DynamicLookupSerializer)
    }
}

/**
 * Use this method to get data for high score tables. Will return the score of the specified user and several of their neighbors in a game. Returns an Array of GameHighScore objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getgamehighscores)
 * @param userId Target user id
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat
 * @param messageId Required if inline_message_id is not specified. Identifier of the sent message
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @returns [Array of GameHighScore]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(userId: Long, messageId: Long) =
    GetGameHighScoresAction(Identifier.from(userId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(userId: Long) = GetGameHighScoresAction(Identifier.from(userId))

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(user: User, messageId: Long) = getGameHighScores(user.id, messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun getGameHighScores(user: User) = getGameHighScores(user.id)
