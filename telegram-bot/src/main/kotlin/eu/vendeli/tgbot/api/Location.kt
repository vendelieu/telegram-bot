@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.LocationOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendLocationAction(
    latitude: Float,
    longitude: Float,
) : Action<Message>,
    ActionState(),
    OptionsFeature<SendLocationAction, LocationOptions>,
    MarkupFeature<SendLocationAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendLocation")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendLocationAction, LocationOptions>.options: LocationOptions
        get() = LocationOptions()

    init {
        parameters["latitude"] = latitude
        parameters["longitude"] = longitude
    }
}

fun sendLocation(latitude: Float, longitude: Float) = location(latitude, longitude)
fun location(latitude: Float, longitude: Float) = SendLocationAction(latitude, longitude)
