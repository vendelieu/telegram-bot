@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.user.Gifts
import eu.vendeli.tgbot.utils.getReturnType

@TgAPI
class GetAvailableGiftsAction : SimpleAction<Gifts>() {
    @TgAPI.Name("getAvailableGifts")
    override val method = "getAvailableGifts"
    override val returnType = getReturnType()
}

/**
 * Returns the list of gifts that can be sent by the bot to users. Requires no parameters. Returns a Gifts object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getavailablegifts)
 *
 * @returns [Gifts]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getAvailableGifts() = GetAvailableGiftsAction()
