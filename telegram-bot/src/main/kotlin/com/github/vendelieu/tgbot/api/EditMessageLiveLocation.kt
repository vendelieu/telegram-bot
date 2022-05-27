package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.EditMessageLiveLocationOptions

class EditMessageLiveLocationAction :
    Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<EditMessageLiveLocationAction, EditMessageLiveLocationOptions>,
    MarkupFeature<EditMessageLiveLocationAction> {
    override val method: TgMethod = TgMethod("editMessageLiveLocation")
    override var options = EditMessageLiveLocationOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(messageId: Long, latitude: Float, longitude: Float) {
        parameters["message_id"] = messageId
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
    }

    constructor(latitude: Float, longitude: Float) {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
    }
}

fun editMessageLiveLocation(messageId: Long, latitude: Float, longitude: Float) =
    EditMessageLiveLocationAction(messageId, latitude, longitude)

fun editMessageLiveLocation(latitude: Float, longitude: Float) = EditMessageLiveLocationAction(latitude, longitude)
