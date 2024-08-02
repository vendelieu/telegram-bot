@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.SetGameScoreOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetGameScoreAction :
    Action<Message>,
    InlineActionExt<Message>,
    OptionsFeature<SetGameScoreAction, SetGameScoreOptions> {
    override val method = TgMethod("setGameScore")
    override val returnType = getReturnType()
    override val options = SetGameScoreOptions()

    constructor(userId: Long, messageId: Long, score: Long) {
        parameters["user_id"] = userId.toJsonElement()
        parameters["score"] = score.toJsonElement()
        parameters["message_id"] = messageId.toJsonElement()
    }

    constructor(userId: Long, score: Long) {
        parameters["user_id"] = userId.toJsonElement()
        parameters["score"] = score.toJsonElement()
    }
}

/**
 * Use this method to set the score of the specified user in a game message. On success, if the message is not an inline message, the Message is returned, otherwise True is returned. Returns an error, if the new score is not greater than the user's current score in the chat and force is False.
 *
 * [Api reference](https://core.telegram.org/bots/api#setgamescore)
 * @param userId User identifier
 * @param score New score, must be non-negative
 * @param force Pass True if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters
 * @param disableEditMessage Pass True if the game message should not be automatically edited to include the current scoreboard
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat
 * @param messageId Required if inline_message_id is not specified. Identifier of the sent message
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @returns [Message]|[Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun setGameScore(userId: Long, messageId: Long, score: Long) = SetGameScoreAction(userId, messageId, score)

@Suppress("NOTHING_TO_INLINE")
inline fun setGameScore(userId: Long, score: Long) = SetGameScoreAction(userId, score)

@Suppress("NOTHING_TO_INLINE")
inline fun setGameScore(user: User, score: Long) = setGameScore(user.id, score)

@Suppress("NOTHING_TO_INLINE")
inline fun setGameScore(user: User, messageId: Long, score: Long) = setGameScore(user.id, messageId, score)
