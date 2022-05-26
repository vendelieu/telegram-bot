package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.ContactOptions

class SendContactAction(
    phoneNumber: String,
    firstName: String,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendContactAction, ContactOptions>,
    MarkupFeature<SendContactAction> {
    override val method: TgMethod = TgMethod("sendContact")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
    override var options = ContactOptions()

    init {
        parameters["first_name"] = firstName
        parameters["phone_number"] = phoneNumber
    }
}

fun contact(firstName: String, phoneNumber: String) = SendContactAction(phoneNumber, firstName)
