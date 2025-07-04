package eu.vendeli.tgbot.api.checklist

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.checklist.InputChecklist
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditMessageChecklistAction(
    messageId: Long,
    checklist: InputChecklist,
) : TgAction<Message>(), // specific case since business only action
    BusinessActionExt<Message>,
    MarkupFeature<SendChecklistAction> {
    @TgAPI.Name("editMessageChecklist")
    override val method = "editMessageChecklist"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
        parameters["checklist"] = checklist.encodeWith(InputChecklist.serializer())
    }
}

/**
 * Use this method to edit a checklist on behalf of a connected business account. On success, the edited Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagechecklist)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat
 * @param messageId Unique identifier for the target message
 * @param checklist A JSON-serialized object for the new checklist
 * @param replyMarkup A JSON-serialized object for the new inline keyboard for the message
 * @returns [Message]
 */
@TgAPI
inline fun editMessageChecklist(messageId: Long, checklist: InputChecklist) =
    EditMessageChecklistAction(messageId, checklist)
