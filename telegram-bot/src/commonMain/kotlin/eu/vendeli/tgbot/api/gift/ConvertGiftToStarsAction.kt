@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class ConvertGiftToStarsAction(
    businessConnectionId: String,
    ownedGiftId: String,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("convertGiftToStars")
    override val method = "convertGiftToStars"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["owned_gift_id"] = ownedGiftId.toJsonElement()
    }
}

/**
 * Converts a given regular gift to Telegram Stars. Requires the can_convert_gifts_to_stars business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#convertgifttostars)
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be converted to Telegram Stars
 * @returns [Boolean]
 */
@TgAPI
inline fun convertGiftToStars(
    businessConnectionId: String,
    ownedGiftId: String,
) = ConvertGiftToStarsAction(businessConnectionId, ownedGiftId)
