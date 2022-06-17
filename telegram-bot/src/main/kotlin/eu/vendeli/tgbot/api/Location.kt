package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.LocationOptions

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
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
    }
}

fun location(latitude: Float, longitude: Float) = SendLocationAction(latitude, longitude)
