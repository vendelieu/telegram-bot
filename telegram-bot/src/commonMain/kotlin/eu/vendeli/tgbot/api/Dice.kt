@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.DiceOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendDiceAction(
    emoji: String? = null,
) : Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendDiceAction, DiceOptions>,
    MarkupFeature<SendDiceAction> {
    @TgAPI.Name("sendDice")
    override val method = "sendDice"
    override val returnType = getReturnType()
    override val options = DiceOptions()

    init {
        if (emoji != null) parameters["emoji"] = emoji.toJsonElement()
    }
}

/**
 * Use this method to send an animated emoji that will display a random value. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#senddice)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param emoji Emoji on which the dice throw animation is based. Currently, must be one of "🎲", "🎯", "🏀", "⚽", "🎳", or "🎰". Dice can have values 1-6 for "🎲", "🎯" and "🎳", values 1-5 for "🏀" and "⚽", and values 1-64 for "🎰". Defaults to "🎲"
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendDice(emoji: String? = null) = dice(emoji)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun dice(emoji: String? = null) = SendDiceAction(emoji)
