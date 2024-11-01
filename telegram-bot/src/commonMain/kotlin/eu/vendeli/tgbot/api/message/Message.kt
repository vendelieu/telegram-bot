@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.MessageOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendMessageAction private constructor() :
    Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendMessageAction, MessageOptions>,
    MarkupFeature<SendMessageAction>,
    EntitiesFeature<SendMessageAction>,
    EntitiesCtxBuilder<SendMessageAction> {
        @TgAPI.Name("sendMessage")
        override val method = "sendMessage"
        override val returnType = getReturnType()
        override val options = MessageOptions()

        constructor(text: String) : this() {
            parameters["text"] = text.toJsonElement()
        }

        internal constructor(block: EntitiesCtxBuilder<SendMessageAction>.() -> String) : this() {
            parameters["text"] = block.invoke(this).toJsonElement()
        }
    }

/**
 * Use this method to send text messages. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendmessage)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param text Text of the message to be sent, 1-4096 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @param linkPreviewOptions Link preview generation options for the message
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun message(text: String) = SendMessageAction(text)

@TgAPI
fun message(block: EntitiesCtxBuilder<SendMessageAction>.() -> String) = SendMessageAction(block)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendMessage(text: String) = message(text)

@TgAPI
fun sendMessage(block: EntitiesCtxBuilder<SendMessageAction>.() -> String) = message(block)
