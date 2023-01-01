@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditMessageLiveLocationOptions

class EditMessageLiveLocationAction :
    Action<Message>,
    InlineMode<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<EditMessageLiveLocationAction, EditMessageLiveLocationOptions>,
    MarkupFeature<EditMessageLiveLocationAction> {
    override val method: TgMethod = TgMethod("editMessageLiveLocation")
    override var options = EditMessageLiveLocationOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

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
