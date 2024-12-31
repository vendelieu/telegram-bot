@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.options.GetUpdatesOptions
import eu.vendeli.tgbot.utils.getReturnType

@TgAPI
class GetUpdatesAction :
    SimpleAction<List<ProcessedUpdate>>(),
    OptionsFeature<GetUpdatesAction, GetUpdatesOptions> {
    @TgAPI.Name("getUpdates")
    override val method = "getUpdates"
    override val returnType = getReturnType()
    override val options = GetUpdatesOptions()
}

/**
 * Use this method to receive incoming updates using long polling (wiki). Returns an Array of Update objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getupdates)
 * @param offset Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue. All previous updates will be forgotten.
 * @param limit Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling should be used for testing purposes only.
 * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify ["message", "edited_channel_post", "callback_query"] to only receive updates of these types. See Update for a complete list of available update types. Specify an empty list to receive all update types except chat_member, message_reaction, and message_reaction_count (default). If not specified, the previous setting will be used. Please note that this parameter doesn't affect updates created before the call to getUpdates, so unwanted updates may be received for a short period of time.
 * @returns [Array of Update]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getUpdates() = GetUpdatesAction()
