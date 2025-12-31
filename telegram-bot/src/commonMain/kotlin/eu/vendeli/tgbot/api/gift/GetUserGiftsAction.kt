@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.gift.OwnedGifts
import eu.vendeli.tgbot.types.options.GetUserGiftsOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetUserGiftsAction(
    userId: Long,
) : SimpleAction<OwnedGifts>(),
    OptionsFeature<GetUserGiftsAction, GetUserGiftsOptions> {
    @TgAPI.Name("getUserGifts")
    override val method = "getUserGifts"
    override val returnType = getReturnType()
    override val options = GetUserGiftsOptions()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Returns the gifts owned and hosted by a user. Returns OwnedGifts on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getusergifts)
 * @param userId Unique identifier of the user
 * @param excludeUnlimited Pass True to exclude gifts that can be purchased an unlimited number of times
 * @param excludeLimitedUpgradable Pass True to exclude gifts that can be purchased a limited number of times and can be upgraded to unique
 * @param excludeLimitedNonUpgradable Pass True to exclude gifts that can be purchased a limited number of times and can't be upgraded to unique
 * @param excludeFromBlockchain Pass True to exclude gifts that were assigned from the TON blockchain and can't be resold or transferred in Telegram
 * @param excludeUnique Pass True to exclude unique gifts
 * @param sortByPrice Pass True to sort results by gift price instead of send date. Sorting is applied before pagination.
 * @param offset Offset of the first entry to return as received from the previous request; use an empty string to get the first chunk of results
 * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100
 * @returns [OwnedGifts]
 */
@TgAPI
inline fun getUserGifts(userId: Long) = GetUserGiftsAction(userId)

@TgAPI
inline fun getUserGifts(user: User) = getUserGifts(user.id)

