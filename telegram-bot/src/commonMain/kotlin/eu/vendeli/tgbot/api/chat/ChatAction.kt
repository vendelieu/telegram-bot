@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.BusinessActionExt
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendChatAction(
    action: ChatAction,
    messageThreadId: Int? = null,
) : Action<Boolean>(),
    BusinessActionExt<Boolean> {
    override val method = TgMethod("sendChatAction")
    override val returnType = getReturnType()

    init {
        parameters["action"] = action.encodeWith(ChatAction.serializer())
        if (messageThreadId != null) parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on success.
 * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendchataction)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the action will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread; for supergroups only
 * @param action Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text messages, upload_photo for photos, record_video or upload_video for videos, record_voice or upload_voice for voice notes, upload_document for general files, choose_sticker for stickers, find_location for location data, record_video_note or upload_video_note for video notes.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun chatAction(action: ChatAction, messageThreadId: Int? = null) = SendChatAction(action, messageThreadId)

inline fun chatAction(messageThreadId: Int? = null, block: () -> ChatAction) = chatAction(block(), messageThreadId)

inline fun sendChatAction(messageThreadId: Int? = null, block: () -> ChatAction) = chatAction(block(), messageThreadId)

@Suppress("NOTHING_TO_INLINE")
inline fun sendChatAction(action: ChatAction, messageThreadId: Int? = null) = chatAction(action, messageThreadId)
