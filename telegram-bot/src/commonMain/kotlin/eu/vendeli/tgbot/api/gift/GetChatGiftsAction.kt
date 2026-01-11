@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.gift

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.types.gift.OwnedGifts
import eu.vendeli.tgbot.types.options.GetChatGiftsOptions
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer

@TgAPI
class GetChatGiftsAction(
    chatId: Identifier,
) : SimpleAction<OwnedGifts>(),
    OptionsFeature<GetChatGiftsAction, GetChatGiftsOptions> {
    @TgAPI.Name("getChatGifts")
    override val method = "getChatGifts"
    override val returnType = getReturnType()
    override val options = GetChatGiftsOptions()

    init {
        parameters["chat_id"] = chatId.encodeWith(DynamicLookupSerializer)
    }
}

/**
 * Returns the gifts owned by a chat. Returns OwnedGifts on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatgifts)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param excludeUnsaved Pass True to exclude gifts that aren't saved to the chat's profile page. Always True, unless the bot has the can_post_messages administrator right in the channel.
 * @param excludeSaved Pass True to exclude gifts that are saved to the chat's profile page. Always False, unless the bot has the can_post_messages administrator right in the channel.
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
inline fun getChatGifts(chatId: String) = GetChatGiftsAction(Identifier.from(chatId))

@TgAPI
inline fun getChatGifts(chatId: Long) = GetChatGiftsAction(Identifier.from(chatId))

@TgAPI
inline fun getChatGifts(chat: Chat) = getChatGifts(chat.id)
