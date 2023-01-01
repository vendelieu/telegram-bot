@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ContactOptions

class SendContactAction(
    phoneNumber: String,
    firstName: String,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendContactAction, ContactOptions>,
    MarkupFeature<SendContactAction> {
    override val method: TgMethod = TgMethod("sendContact")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override var options = ContactOptions()

    init {
        parameters["first_name"] = firstName
        parameters["phone_number"] = phoneNumber
    }
}

fun contact(firstName: String, phoneNumber: String) = SendContactAction(phoneNumber, firstName)
