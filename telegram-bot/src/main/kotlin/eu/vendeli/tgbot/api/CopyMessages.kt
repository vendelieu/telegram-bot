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

class CopyMessagesAction(
    fromChatId: Identifier<*>,
    messageIds: List<Long>,
) : Action<MessageId>(),
    OptionsFeature<CopyMessagesAction, CopyMessageOptions>,
    MarkupFeature<CopyMessagesAction>,
    CaptionFeature<CopyMessagesAction> {
    override val method = TgMethod("copyMessage")
    override val returnType = getReturnType()
    override val options = CopyMessageOptions()

    init {
        parameters["from_chat_id"] = fromChatId.get
        parameters["message_ids"] = messageIds
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun copyMessages(fromChatId: Identifier<*>, messageIds: List<Long>) =
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
