@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.options.RichMessageOptions
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.transform

@TgAPI
class SendRichMessageAction(
    richMessage: InputRichMessage,
) : Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendRichMessageAction, RichMessageOptions>,
    MarkupFeature<SendRichMessageAction> {
    @TgAPI.Name("sendRichMessage")
    override val method = "sendRichMessage"
    override val returnType = getReturnType()
    override val options = RichMessageOptions()

    init {
        richMessage.media?.forEach { it.media.media = it.media.media.transform(multipartData) }
        parameters["rich_message"] = richMessage.encodeWith(InputRichMessage.serializer())
    }
}

/**
 * Use this method to send rich messages. If the message contains a block with a media element, then the bot must have the right to send the media to the chat. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendrichmessage)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent. Bot can send rich messages on behalf of a business account only if the corresponding user can send rich messages.
 * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel in the format @username
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param ephemeralMessageParameters A JSON-serialized object containing the parameters of the ephemeral message to send
 * @param richMessage The message to be sent
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user.
 * @returns [Message]
 */
@TgAPI
inline fun richMessage(richMessage: InputRichMessage) = SendRichMessageAction(richMessage)

@TgAPI
fun richMessage(block: InputRichMessage.() -> Unit) = SendRichMessageAction(InputRichMessage().apply(block))

@TgAPI
inline fun sendRichMessage(richMessage: InputRichMessage) = richMessage(richMessage)

@TgAPI
fun sendRichMessage(block: InputRichMessage.() -> Unit) = richMessage(block)
