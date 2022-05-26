package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.VenueParams
import com.github.vendelieu.tgbot.types.internal.options.VenueOptions

class SendVenueAction(
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendVenueAction, VenueOptions>,
    MarkupFeature<SendVenueAction> {
    override val method: TgMethod = TgMethod("sendVenue")
    override var options = VenueOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
        parameters["title"] = title
        parameters["address"] = address
    }
}

fun venue(latitude: Float, longitude: Float, params: VenueParams.() -> Unit): SendVenueAction {
    val venueParams = VenueParams("", "").apply(params)
    return SendVenueAction(latitude, longitude, venueParams.title, venueParams.address)
}

fun venue(latitude: Float, longitude: Float, title: String, address: String) =
    SendVenueAction(latitude, longitude, title, address)
