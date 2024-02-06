
@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VenueOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

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
    override val options = VenueOptions()
    init {
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
        parameters["title"] = title.toJsonElement()
        parameters["address"] = address.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun venue(latitude: Float, longitude: Float, title: String, address: String) =
    SendVenueAction(latitude, longitude, title, address)

@Suppress("NOTHING_TO_INLINE")
inline fun sendVenue(latitude: Float, longitude: Float, title: String, address: String) =
    venue(latitude, longitude, title, address)
