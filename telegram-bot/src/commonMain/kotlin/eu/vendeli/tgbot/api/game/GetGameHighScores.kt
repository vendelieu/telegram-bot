@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.game

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.game.GameHighScore
import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetGameHighScoresAction :
    Action<List<GameHighScore>>,
    InlineActionExt<List<GameHighScore>> {
    @TgAPI.Name("getGameHighScores")
    override val method = "getGameHighScores"
    override val returnType = getReturnType()

    constructor(userId: Identifier, messageId: Long) {
        parameters["user_id"] = userId.encodeWith(DynamicLookupSerializer)
        parameters["message_id"] = messageId.toJsonElement()
    }

    constructor(userId: Identifier) {
        parameters["user_id"] = userId.encodeWith(DynamicLookupSerializer)
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
@TgAPI
inline fun getGameHighScores(userId: Long, messageId: Long) =
    GetGameHighScoresAction(Identifier.from(userId), messageId)

@TgAPI
inline fun getGameHighScores(userId: Long) = GetGameHighScoresAction(Identifier.from(userId))

@TgAPI
inline fun getGameHighScores(user: User, messageId: Long) = getGameHighScores(user.id, messageId)

@TgAPI
inline fun getGameHighScores(user: User) = getGameHighScores(user.id)
