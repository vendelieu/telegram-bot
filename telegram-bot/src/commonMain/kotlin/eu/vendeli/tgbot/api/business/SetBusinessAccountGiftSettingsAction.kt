@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.gift.AcceptedGiftTypes
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetBusinessAccountGiftSettingsAction(
    businessConnectionId: String,
    showGiftButton: Boolean,
    acceptedGiftTypes: AcceptedGiftTypes,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setBusinessAccountGiftSettings")
    override val method = "setBusinessAccountGiftSettings"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["show_gift_button"] = showGiftButton.toJsonElement()
        parameters["accepted_gift_types"] = acceptedGiftTypes.encodeWith(AcceptedGiftTypes.serializer())
    }
}

/**
 * Changes the privacy settings pertaining to incoming gifts in a managed business account. Requires the can_change_gift_settings business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setbusinessaccountgiftsettings)
 * @param businessConnectionId Unique identifier of the business connection
 * @param showGiftButton Pass True, if a button for sending a gift to the user or by the business account must always be shown in the input field
 * @param acceptedGiftTypes Types of gifts accepted by the business account
 * @returns [Boolean]
 */
@TgAPI
inline fun setBusinessAccountGiftSettings(
    businessConnectionId: String,
    showGiftButton: Boolean,
    acceptedGiftTypes: AcceptedGiftTypes,
) = SetBusinessAccountGiftSettingsAction(businessConnectionId, showGiftButton, acceptedGiftTypes)
