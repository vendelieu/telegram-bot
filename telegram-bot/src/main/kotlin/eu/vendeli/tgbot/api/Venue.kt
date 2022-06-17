package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.VenueParams
import eu.vendeli.tgbot.types.internal.options.VenueOptions

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
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

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
