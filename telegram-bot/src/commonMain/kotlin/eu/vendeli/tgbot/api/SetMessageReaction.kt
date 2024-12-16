@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.builtins.ListSerializer

@TgAPI
class SetMessageReactionAction(
    messageId: Long,
    reaction: List<ReactionType>? = null,
    isBig: Boolean? = null,
) : Action<Boolean>() {
    @TgAPI.Name("setMessageReaction")
    override val method = "setMessageReaction"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
        if (reaction != null) parameters["reaction"] = reaction.encodeWith(ListSerializer(ReactionType.serializer()))
        if (isBig != null) parameters["is_big"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to change the chosen reactions on a message. Service messages can't be reacted to. Automatically forwarded messages from a channel to its discussion group have the same available reactions as messages in the channel. Bots can't use paid reactions. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmessagereaction)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of the target message. If the message belongs to a media group, the reaction is set to the first non-deleted message in the group instead.
 * @param reaction A JSON-serialized list of reaction types to set on the message. Currently, as non-premium users, bots can set up to one reaction per message. A custom emoji reaction can be used if it is either already present on the message or explicitly allowed by chat administrators. Paid reactions can't be used by bots.
 * @param isBig Pass True to set the reaction with a big animation
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setMessageReaction(messageId: Long, reaction: List<ReactionType>? = null, isBig: Boolean? = null) =
    SetMessageReactionAction(messageId, reaction, isBig)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setMessageReaction(messageId: Long, vararg reaction: ReactionType, isBig: Boolean? = null) =
    setMessageReaction(messageId, reaction.asList(), isBig)

@TgAPI
fun setMessageReaction(
    messageId: Long,
    isBig: Boolean? = null,
    reaction: ListingBuilder<ReactionType>.() -> Unit,
) = setMessageReaction(messageId, ListingBuilder.build(reaction), isBig)
