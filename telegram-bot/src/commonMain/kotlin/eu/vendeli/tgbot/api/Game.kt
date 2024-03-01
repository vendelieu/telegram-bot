@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.GameOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendGameAction(
    gameShortName: String,
) : Action<Message>(),
    OptionsFeature<SendGameAction, GameOptions>,
    MarkupFeature<SendGameAction> {
    override val method = TgMethod("sendGame")
    override val returnType = getReturnType()
    override val options = GameOptions()

    init {
        parameters["game_short_name"] = gameShortName.toJsonElement()
    }
}

/**
 * Use this method to send a game. On success, the sent Message is returned.
 * Api reference: https://core.telegram.org/bots/api#sendgame
 * @param chatId Unique identifier for the target chat
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param gameShortName Short name of the game, serves as the unique identifier for the game. Set up your games via @BotFather.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard. If empty, one 'Play game_title' button will be shown. If not empty, the first button must launch the game.
 * @returns [Message]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun sendGame(gameShortName: String) = game(gameShortName)

@Suppress("NOTHING_TO_INLINE")
inline fun game(gameShortName: String) = SendGameAction(gameShortName)
