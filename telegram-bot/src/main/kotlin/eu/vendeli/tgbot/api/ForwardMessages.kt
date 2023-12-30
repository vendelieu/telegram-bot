@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.MessageId
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ForwardMessageOptions
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class ForwardMessagesAction(fromChatId: Identifier<*>, messageIds: List<Long>) :
    Action<List<MessageId>>(),
    OptionsFeature<ForwardMessagesAction, ForwardMessageOptions> {
    override val method = TgMethod("forwardMessages")
    override val returnType = getReturnType()
    override val wrappedDataType = getInnerType()
    override val options = ForwardMessageOptions()

    init {
        parameters["from_chat_id"] = fromChatId.get
        parameters["message_ids"] = messageIds
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: Identifier<*>, messageIds: List<Long>) =
    ForwardMessagesAction(fromChatId, messageIds)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: Long, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: String, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: User, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: Chat, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId.id), messageId.asList())
