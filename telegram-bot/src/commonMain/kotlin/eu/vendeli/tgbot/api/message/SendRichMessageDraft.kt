@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.options.SendRichMessageDraftOptions
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SendRichMessageDraftAction(
    draftId: Int,
    richMessage: InputRichMessage,
) : Action<Boolean>(),
    OptionsFeature<SendRichMessageDraftAction, SendRichMessageDraftOptions> {
    @TgAPI.Name("sendRichMessageDraft")
    override val method = "sendRichMessageDraft"
    override val returnType = getReturnType()
    override val options = SendRichMessageDraftOptions()

    init {
        parameters["draft_id"] = draftId.toJsonElement()
        parameters["rich_message"] = richMessage.encodeWith(InputRichMessage.serializer())
    }
}

/**
 * Use this method to stream a partial rich message to a user while the message is being generated. Note that the streamed draft is ephemeral and acts as a temporary 30-second preview - once the output is finalized, you must call sendRichMessage with the complete message to persist it in the user's chat. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendrichmessagedraft)
 * @param chatId Unique identifier for the target private chat
 * @param messageThreadId Unique identifier for the target message thread
 * @param draftId Unique identifier of the message draft; must be non-zero. Changes to drafts with the same identifier are animated. Otherwise, the draft is replaced without animation.
 * @param richMessage The partial message to be streamed. Direct upload of new files and explicit upload of files by a URL isn't supported.
 * @param canStop Pass True to show the user a button to stop further drafts. The bot will receive an Update "stopped_message_generation" if the user presses the button.
 * @param keepOnStop Pass True to keep the draft in the chat when the button is pressed. The draft will still disappear after a short time or if the bot sends a message. To fully preserve the partial draft, the bot should send it as a new message.
 * @returns [Boolean]
 */
@TgAPI
inline fun sendRichMessageDraft(draftId: Int, richMessage: InputRichMessage) =
    SendRichMessageDraftAction(draftId, richMessage)

@TgAPI
fun sendRichMessageDraft(draftId: Int, block: InputRichMessage.() -> Unit) =
    SendRichMessageDraftAction(draftId, InputRichMessage().apply(block))
