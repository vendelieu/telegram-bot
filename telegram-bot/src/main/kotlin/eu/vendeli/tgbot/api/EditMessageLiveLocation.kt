@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditMessageLiveLocationOptions
import eu.vendeli.tgbot.utils.getReturnType

class EditMessageLiveLocationAction :
    Action<Message>,
    ActionState,
    InlineMode<Message>,
    OptionsFeature<EditMessageLiveLocationAction, EditMessageLiveLocationOptions>,
    MarkupFeature<EditMessageLiveLocationAction> {
    override val method: TgMethod = TgMethod("editMessageLiveLocation")
    override val returnType = getReturnType()
    override val OptionsFeature<EditMessageLiveLocationAction, EditMessageLiveLocationOptions>.options: EditMessageLiveLocationOptions
        get() = EditMessageLiveLocationOptions()

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
