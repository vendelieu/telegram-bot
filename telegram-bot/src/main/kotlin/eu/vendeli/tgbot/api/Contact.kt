@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ContactOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendContactAction(
    phoneNumber: String,
    firstName: String,
) : Action<Message>,
    ActionState(),
    OptionsFeature<SendContactAction, ContactOptions>,
    MarkupFeature<SendContactAction> {
    override val method: TgMethod = TgMethod("sendContact")
    override val returnType = getReturnType()
    override var options = ContactOptions()

    init {
        parameters["first_name"] = firstName
        parameters["phone_number"] = phoneNumber
    }
}

fun contact(firstName: String, phoneNumber: String) = SendContactAction(phoneNumber, firstName)
