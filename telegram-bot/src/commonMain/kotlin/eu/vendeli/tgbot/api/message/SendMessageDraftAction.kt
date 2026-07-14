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
            draftId: Int,
            text: String,
        ) : this() {
            parameters["draft_id"] = draftId.toJsonElement()
            parameters["text"] = text.toJsonElement()
        }

        internal constructor(
            draftId: Int,
            block: EntitiesCtxBuilder<SendMessageDraftAction>.() -> String,
        ) : this() {
            parameters["draft_id"] = draftId.toJsonElement()
            parameters["text"] = block.invoke(this).toJsonElement()
        }
    }

/**
 * Use this method to stream a partial message to a user while the message is being generated. Note that the streamed draft is ephemeral and acts as a temporary 30-second preview - once the output is finalized, you must call sendMessage with the complete message to persist it in the user's chat. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendmessagedraft)
 * @param chatId Unique identifier for the target private chat
 * @param messageThreadId Unique identifier for the target message thread
 * @param draftId Unique identifier of the message draft; must be non-zero. Changes to drafts with the same identifier are animated.
 * @param text Text of the message to be sent, 0-4096 characters after entities parsing. Pass an empty text to show a "Thinking..." placeholder.
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @returns [Boolean]
 */
@TgAPI
inline fun sendMessageDraft(
    draftId: Int,
    text: String,
) = SendMessageDraftAction(draftId, text)

@TgAPI
fun sendMessageDraft(
    draftId: Int,
    block: EntitiesCtxBuilder<SendMessageDraftAction>.() -> String,
) = SendMessageDraftAction(draftId, block)
