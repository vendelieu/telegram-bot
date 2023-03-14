@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.MessageId
import eu.vendeli.tgbot.types.internal.Recipient
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CopyMessageOptions
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
 * A quiz poll can be copied only if the value of the field correct_option_id is known to the bot.
 * The method is analogous to the method forwardMessage,
 * but the copied message doesn't have a link to the original message.
 *
 * @param chatId Unique identifier for the target chat or username
 * of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent
 * (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in fromChatId
 */
class CopyMessageAction(
    chatId: Recipient,
    fromChatId: Recipient,
    messageId: Long,
) :
    Action<MessageId>,
    ActionState(),
    OptionsFeature<CopyMessageAction, CopyMessageOptions>,
    MarkupFeature<CopyMessageAction>,
    EntitiesContextBuilder,
    CaptionFeature<CopyMessageAction> {
    override val TgAction<MessageId>.method: TgMethod
        get() = TgMethod("copyMessage")
    override val TgAction<MessageId>.returnType: Class<MessageId>
        get() = getReturnType()
    override val OptionsFeature<CopyMessageAction, CopyMessageOptions>.options: CopyMessageOptions
        get() = CopyMessageOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

    init {
        parameters["chat_id"] = chatId.get()
        parameters["from_chat_id"] = fromChatId.get()
        parameters["message_id"] = messageId
    }
}

/**
 * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
 * A quiz poll can be copied only if the value of the field correct_option_id is known to the bot.
 * The method is analogous to the method forwardMessage,
 * but the copied message doesn't have a link to the original message.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 * (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent
 * (or channel username in the format @channelusername)
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
