package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.LocationOptions

class SendLocationAction(
    latitude: Float,
    longitude: Float,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendLocationAction, LocationOptions>,
    MarkupFeature<SendLocationAction> {
    override val method: TgMethod = TgMethod("sendLocation")
    override var options = LocationOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
    }
}

fun location(latitude: Float, longitude: Float) = SendLocationAction(latitude, longitude)
