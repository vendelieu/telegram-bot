@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.gift.UniqueGift
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetUniqueGiftAction(
    name: String,
) : SimpleAction<UniqueGift>() {
    @TgAPI.Name("getUniqueGift")
    override val method = "getUniqueGift"
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
    }
}

/**
 * Use this method to get information about a unique gift by its name. Returns a UniqueGift object on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getuniquegift)
 * @param name Unique name of the gift
 * @returns [UniqueGift]
 */
@TgAPI
inline fun getUniqueGift(name: String) = GetUniqueGiftAction(name)
