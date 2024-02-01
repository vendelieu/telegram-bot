@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditMessageLiveLocationOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditMessageLiveLocationAction :
    InlinableAction<Message>,
    OptionsFeature<EditMessageLiveLocationAction, EditMessageLiveLocationOptions>,
    MarkupFeature<EditMessageLiveLocationAction> {
    override val method = TgMethod("editMessageLiveLocation")
    override val returnType = getReturnType()
    override val options = EditMessageLiveLocationOptions()

    constructor(messageId: Long, latitude: Float, longitude: Float) {
        parameters["message_id"] = messageId.toJsonElement()
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
    }

    constructor(latitude: Float, longitude: Float) {
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
    }
}

inline fun editMessageLiveLocation(messageId: Long, latitude: Float, longitude: Float) =
    EditMessageLiveLocationAction(messageId, latitude, longitude)

inline fun editMessageLiveLocation(
    latitude: Float,
    longitude: Float,
) = EditMessageLiveLocationAction(latitude, longitude)
