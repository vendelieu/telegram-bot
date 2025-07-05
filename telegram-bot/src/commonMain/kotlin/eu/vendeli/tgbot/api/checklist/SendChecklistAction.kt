package eu.vendeli.tgbot.api.checklist

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.checklist.InputChecklist
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.options.SendChecklistOptions
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class SendChecklistAction(
    checklist: InputChecklist,
) : TgAction<Message>(), // specific case since business only action
    BusinessActionExt<Message>,
    OptionsFeature<SendChecklistAction, SendChecklistOptions>,
    MarkupFeature<SendChecklistAction> {
    @TgAPI.Name("sendChecklist")
    override val method = "sendChecklist"
    override val returnType = getReturnType()
    override val options = SendChecklistOptions()

    init {
        parameters["checklist"] = checklist.encodeWith(InputChecklist.serializer())
    }
}

/**
 * Use this method to send a checklist on behalf of a connected business account. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendchecklist)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat
 * @param checklist A JSON-serialized object for the checklist to send
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message
 * @param replyParameters A JSON-serialized object for description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard
 * @returns [Message]
 */
@TgAPI
inline fun sendChecklist(checklist: InputChecklist) = SendChecklistAction(checklist)
