@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.game

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.GameOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SendGameAction(
    gameShortName: String,
) : Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendGameAction, GameOptions>,
    MarkupFeature<SendGameAction> {
    @TgAPI.Name("sendGame")
    override val method = "sendGame"
    override val returnType = getReturnType()
    override val options = GameOptions()

    init {
        parameters["game_short_name"] = gameShortName.toJsonElement()
    }
}

/**
 * Use this method to send a game. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendgame)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat. Games can't be sent to channel direct messages chats and channel chats.
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param gameShortName Short name of the game, serves as the unique identifier for the game. Set up your games via @BotFather.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard. If empty, one 'Play game_title' button will be shown. If not empty, the first button must launch the game.
 * @returns [Message]
 */
@TgAPI
inline fun sendGame(gameShortName: String) = game(gameShortName)

@TgAPI
inline fun game(gameShortName: String) = SendGameAction(gameShortName)
