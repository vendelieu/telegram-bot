@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.SendMessageDraftOptions
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SendMessageDraftAction private constructor() :
    Action<Boolean>(),
    OptionsFeature<SendMessageDraftAction, SendMessageDraftOptions>,
    EntitiesFeature<SendMessageDraftAction>,
    EntitiesCtxBuilder<SendMessageDraftAction> {
        @TgAPI.Name("sendMessageDraft")
        override val method = "sendMessageDraft"
        override val returnType = getReturnType()
        override val options = SendMessageDraftOptions()

        constructor(
            chatId: Long,
            draftId: Int,
            text: String,
        ) : this() {
            parameters["chat_id"] = chatId.toJsonElement()
            parameters["draft_id"] = draftId.toJsonElement()
            parameters["text"] = text.toJsonElement()
        }

        internal constructor(
            chatId: Long,
            draftId: Int,
            block: EntitiesCtxBuilder<SendMessageDraftAction>.() -> String,
        ) : this() {
            parameters["chat_id"] = chatId.toJsonElement()
            parameters["draft_id"] = draftId.toJsonElement()
            parameters["text"] = block.invoke(this).toJsonElement()
        }
    }

/**
 * Use this method to stream a partial message to a user while the message is being generated; supported only for bots with forum topic mode enabled. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendmessagedraft)
 * @param chatId Unique identifier for the target private chat
 * @param messageThreadId Unique identifier for the target message thread
 * @param draftId Unique identifier of the message draft; must be non-zero. Changes of drafts with the same identifier are animated
 * @param text Text of the message to be sent, 1-4096 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @returns [Boolean]
 */
@TgAPI
inline fun sendMessageDraft(
    chatId: Long,
    draftId: Int,
    text: String,
) = SendMessageDraftAction(chatId, draftId, text)

@TgAPI
fun sendMessageDraft(
    chatId: Long,
    draftId: Int,
    block: EntitiesCtxBuilder<SendMessageDraftAction>.() -> String,
) = SendMessageDraftAction(chatId, draftId, block)
