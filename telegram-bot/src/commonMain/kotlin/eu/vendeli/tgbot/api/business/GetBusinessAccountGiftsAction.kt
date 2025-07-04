@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.gift.OwnedGifts
import eu.vendeli.tgbot.types.options.GetBusinessAccountGiftsOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetBusinessAccountGiftsAction(
    businessConnectionId: String,
) : SimpleAction<OwnedGifts>(),
    OptionsFeature<GetBusinessAccountGiftsAction, GetBusinessAccountGiftsOptions> {
    @TgAPI.Name("getBusinessAccountGifts")
    override val method = "getBusinessAccountGifts"
    override val returnType = getReturnType()
    override val options = GetBusinessAccountGiftsOptions()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
    }
}

/**
 * Returns the gifts received and owned by a managed business account. Requires the can_view_gifts_and_stars business bot right. Returns OwnedGifts on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getbusinessaccountgifts)
 * @param businessConnectionId Unique identifier of the business connection
 * @param excludeUnsaved Pass True to exclude gifts that aren't saved to the account's profile page
 * @param excludeSaved Pass True to exclude gifts that are saved to the account's profile page
 * @param excludeUnlimited Pass True to exclude gifts that can be purchased an unlimited number of times
 * @param excludeLimited Pass True to exclude gifts that can be purchased a limited number of times
 * @param excludeUnique Pass True to exclude unique gifts
 * @param sortByPrice Pass True to sort results by gift price instead of send date. Sorting is applied before pagination.
 * @param offset Offset of the first entry to return as received from the previous request; use empty string to get the first chunk of results
 * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100
 * @returns [OwnedGifts]
 */
@TgAPI
inline fun getBusinessAccountGifts(
    businessConnectionId: String,
) = GetBusinessAccountGiftsAction(businessConnectionId)
