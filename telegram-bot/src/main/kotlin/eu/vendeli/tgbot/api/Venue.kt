@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VenueOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendVenueAction(
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
) : Action<Message>(),
    OptionsFeature<SendVenueAction, VenueOptions>,
    MarkupFeature<SendVenueAction> {
    override val method = TgMethod("sendVenue")
    override val returnType = getReturnType()
    override val OptionsFeature<SendVenueAction, VenueOptions>.options: VenueOptions
        get() = VenueOptions()
    init {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
        parameters["title"] = title
        parameters["address"] = address
    }
}

fun sendVenue(latitude: Float, longitude: Float, title: String, address: String) =
    venue(latitude, longitude, title, address)
fun venue(latitude: Float, longitude: Float, title: String, address: String) =
    SendVenueAction(latitude, longitude, title, address)
