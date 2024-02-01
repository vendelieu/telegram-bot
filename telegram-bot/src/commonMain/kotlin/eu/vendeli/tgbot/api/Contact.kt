@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ContactOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendContactAction(
    phoneNumber: String,
    firstName: String,
) : Action<Message>(),
    OptionsFeature<SendContactAction, ContactOptions>,
    MarkupFeature<SendContactAction> {
    override val method = TgMethod("sendContact")
    override val returnType = getReturnType()
    override val options = ContactOptions()

    init {
        parameters["first_name"] = firstName.toJsonElement()
        parameters["phone_number"] = phoneNumber.toJsonElement()
    }
}

inline fun contact(firstName: String, phoneNumber: String) = SendContactAction(phoneNumber, firstName)

inline fun sendContact(firstName: String, phoneNumber: String) = contact(firstName, phoneNumber)
