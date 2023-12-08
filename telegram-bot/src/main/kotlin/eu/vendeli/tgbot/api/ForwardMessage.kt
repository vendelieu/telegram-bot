@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ForwardMessageOptions
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this action to forward messages of any kind. Service messages can't be forwarded.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 * (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent
 * (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in fromChatId
 */
class ForwardMessageAction(chatId: Identifier<*>, fromChatId: Identifier<*>, messageId: Long) :
    Action<Message>(),
    OptionsFeature<ForwardMessageAction, ForwardMessageOptions> {
    override val method = TgMethod("forwardMessage")
    override val returnType = getReturnType()
    override val options = ForwardMessageOptions()

    init {
        parameters["chat_id"] = chatId.get
        parameters["from_chat_id"] = fromChatId.get
        parameters["message_id"] = messageId
    }
}

/**
 * Use this method to forward messages of any kind. Service messages can't be forwarded.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 * (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent
 * (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in fromChatId
 */
@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(chatId: Long, fromChatId: Long, messageId: Long) =
    ForwardMessageAction(Identifier.from(chatId), Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(chatId: String, fromChatId: Long, messageId: Long) =
    ForwardMessageAction(Identifier.from(chatId), Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(chatId: Long, fromChatId: String, messageId: Long) =
    ForwardMessageAction(Identifier.from(chatId), Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(chatId: String, fromChatId: String, messageId: Long) =
    ForwardMessageAction(Identifier.from(chatId), Identifier.from(fromChatId), messageId)
