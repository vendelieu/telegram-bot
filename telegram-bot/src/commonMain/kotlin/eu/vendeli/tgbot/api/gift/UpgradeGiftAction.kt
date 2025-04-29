@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class UpgradeGiftAction(
    businessConnectionId: String,
    ownedGiftId: String,
    keepOriginalDetails: Boolean? = null,
    starCount: Int? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("upgradeGift")
    override val method = "upgradeGift"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["owned_gift_id"] = ownedGiftId.toJsonElement()
        keepOriginalDetails?.let { parameters["keep_original_details"] = it.toJsonElement() }
        starCount?.let { parameters["star_count"] = it.toJsonElement() }
    }
}

/**
 * Upgrades a given regular gift to a unique gift. Requires the can_transfer_and_upgrade_gifts business bot right. Additionally requires the can_transfer_stars business bot right if the upgrade is paid. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#upgradegift)
 * @param businessConnectionId Unique identifier of the business connection
 * @param ownedGiftId Unique identifier of the regular gift that should be upgraded to a unique one
 * @param keepOriginalDetails Pass True to keep the original gift text, sender and receiver in the upgraded gift
 * @param starCount The amount of Telegram Stars that will be paid for the upgrade from the business account balance. If gift.prepaid_upgrade_star_count > 0, then pass 0, otherwise, the can_transfer_stars business bot right is required and gift.upgrade_star_count must be passed.
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun upgradeGift(
    businessConnectionId: String,
    ownedGiftId: String,
    keepOriginalDetails: Boolean? = null,
    starCount: Int? = null,
) = UpgradeGiftAction(businessConnectionId, ownedGiftId, keepOriginalDetails, starCount)
