package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.MessageId
import eu.vendeli.tgbot.types.internal.Recipient
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CopyMessageOptions

/**
 * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
 * A quiz poll can be copied only if the value of the field correct_option_id is known to the bot.
 * The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in fromChatId
 */
class CopyMessageAction(
    chatId: Recipient,
    fromChatId: Recipient,
    messageId: Long,
) :
    Action<MessageId>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<CopyMessageAction, CopyMessageOptions>,
    MarkupFeature<CopyMessageAction>,
    CaptionFeature<CopyMessageAction> {
    override val method: TgMethod = TgMethod("copyMessage")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override var options = CopyMessageOptions()

    init {
        parameters["chat_id"] = chatId.get()
        parameters["from_chat_id"] = fromChatId.get()
        parameters["message_id"] = messageId
    }
}

/**
 * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
 * A quiz poll can be copied only if the value of the field correct_option_id is known to the bot.
 * The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in fromChatId
 */
fun copyMessage(chatId: Long, fromChatId: Long, messageId: Long) =
    CopyMessageAction(Recipient.from(chatId), Recipient.from(fromChatId), messageId)


fun copyMessage(chatId: String, fromChatId: Long, messageId: Long) =
    CopyMessageAction(Recipient.from(chatId), Recipient.from(fromChatId), messageId)


fun copyMessage(chatId: Long, fromChatId: String, messageId: Long) =
    CopyMessageAction(Recipient.from(chatId), Recipient.from(fromChatId), messageId)


fun copyMessage(chatId: String, fromChatId: String, messageId: Long) =
    CopyMessageAction(Recipient.from(chatId), Recipient.from(fromChatId), messageId)