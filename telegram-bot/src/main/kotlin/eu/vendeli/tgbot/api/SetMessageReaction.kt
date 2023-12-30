@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType

class SetMessageReactionAction(
    messageId: Long,
    reaction: List<ReactionType>? = null,
    isBig: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("setMessageReaction")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId
        if (reaction != null) parameters["reaction"] = reaction
        if (isBig != null) parameters["is_big"] = messageId
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setMessageReaction(messageId: Long, reaction: List<ReactionType>? = null, isBig: Boolean? = null) =
    SetMessageReactionAction(messageId, reaction, isBig)

@Suppress("NOTHING_TO_INLINE")
inline fun setMessageReaction(messageId: Long, vararg reaction: ReactionType, isBig: Boolean? = null) =
    setMessageReaction(messageId, reaction.asList(), isBig)

fun setMessageReaction(
    messageId: Long,
    isBig: Boolean? = null,
    reaction: ListingBuilder<ReactionType>.() -> Unit,
) = setMessageReaction(messageId, ListingBuilder.build(reaction), isBig)
