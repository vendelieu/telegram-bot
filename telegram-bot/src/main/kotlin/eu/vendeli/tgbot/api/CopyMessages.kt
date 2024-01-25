@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.MessageId
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CopyMessagesOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class CopyMessagesAction(
    fromChatId: Identifier,
    messageIds: List<Long>,
) : Action<List<MessageId>>(),
    OptionsFeature<CopyMessagesAction, CopyMessagesOptions> {
    override val method = TgMethod("copyMessages")
    override val returnType = getReturnType()
    override val options = CopyMessagesOptions()

    init {
        parameters["from_chat_id"] = fromChatId.get.toJsonElement()
        parameters["message_ids"] = messageIds.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessages(fromChatId: Identifier, messageIds: List<Long>) =
    CopyMessagesAction(fromChatId, messageIds)

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessages(fromChatId: Long, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessages(fromChatId: String, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessages(fromChatId: User, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessages(fromChatId: Chat, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId.id), messageId.asList())
