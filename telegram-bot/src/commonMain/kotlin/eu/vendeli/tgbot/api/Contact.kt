@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.ContactOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendContactAction(
    phoneNumber: String,
    firstName: String,
) : Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendContactAction, ContactOptions>,
    MarkupFeature<SendContactAction> {
    @TgAPI.Method("sendContact")
    override val method = "sendContact"
    override val returnType = getReturnType()
    override val options = ContactOptions()

    init {
        parameters["first_name"] = firstName.toJsonElement()
        parameters["phone_number"] = phoneNumber.toJsonElement()
    }
}

/**
 * Use this method to send phone contacts. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendcontact)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Contact's last name
 * @param vcard Additional data about the contact in the form of a vCard, 0-2048 bytes
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun contact(firstName: String, phoneNumber: String) = SendContactAction(phoneNumber, firstName)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendContact(firstName: String, phoneNumber: String) = contact(firstName, phoneNumber)
