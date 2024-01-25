@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.MessageId
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CopyMessageOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

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
    fromChatId: Identifier,
    messageId: Long,
) : Action<MessageId>(),
    OptionsFeature<CopyMessageAction, CopyMessageOptions>,
    MarkupFeature<CopyMessageAction>,
    CaptionFeature<CopyMessageAction> {
    override val method = TgMethod("copyMessage")
    override val returnType = getReturnType()
    override val options = CopyMessageOptions()

    init {
        parameters["from_chat_id"] = fromChatId.get.toJsonElement()
        parameters["message_id"] = messageId.toJsonElement()
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
@Suppress("NOTHING_TO_INLINE")
inline fun copyMessage(fromChatId: Identifier, messageId: Long) = CopyMessageAction(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessage(fromChatId: Long, messageId: Long) = copyMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessage(fromChatId: String, messageId: Long) = copyMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessage(fromChatId: User, messageId: Long) = copyMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessage(fromChatId: Chat, messageId: Long) = copyMessage(Identifier.from(fromChatId.id), messageId)
