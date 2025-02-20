@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.options.SavePreparedInlineMessageOptions
import eu.vendeli.tgbot.types.msg.PreparedInlineMessage
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SavePreparedInlineMessageAction(
    userId: Long,
    result: InlineQueryResult,
) : SimpleAction<PreparedInlineMessage>(),
    OptionsFeature<SavePreparedInlineMessageAction, SavePreparedInlineMessageOptions> {
    @TgAPI.Name("savePreparedInlineMessage")
    override val method = "savePreparedInlineMessage"
    override val returnType = getReturnType()
    override val options = SavePreparedInlineMessageOptions()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["result"] = result.encodeWith(InlineQueryResult.serializer())
    }
}

/**
 * Stores a message that can be sent by a user of a Mini App. Returns a PreparedInlineMessage object.
 *
 * [Api reference](https://core.telegram.org/bots/api#savepreparedinlinemessage)
 * @param userId Unique identifier of the target user that can use the prepared message
 * @param result A JSON-serialized object describing the message to be sent
 * @param allowUserChats Pass True if the message can be sent to private chats with users
 * @param allowBotChats Pass True if the message can be sent to private chats with bots
 * @param allowGroupChats Pass True if the message can be sent to group and supergroup chats
 * @param allowChannelChats Pass True if the message can be sent to channel chats
 * @returns [PreparedInlineMessage]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun savePreparedInlineMessage(
    userId: Long,
    result: InlineQueryResult,
) = SavePreparedInlineMessageAction(userId, result)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun savePreparedInlineMessage(
    userId: Long,
    result: () -> InlineQueryResult,
) = SavePreparedInlineMessageAction(userId, result())
