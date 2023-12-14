@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.LocationOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendLocationAction(
    latitude: Float,
    longitude: Float,
) : Action<Message>(),
    OptionsFeature<SendLocationAction, LocationOptions>,
    MarkupFeature<SendLocationAction> {
    override val method = TgMethod("sendLocation")
    override val returnType = getReturnType()
    override val options = LocationOptions()

    init {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun sendLocation(latitude: Float, longitude: Float) = location(latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
inline fun location(latitude: Float, longitude: Float) = SendLocationAction(latitude, longitude)
