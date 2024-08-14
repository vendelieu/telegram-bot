@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import eu.vendeli.tgbot.utils.toJsonElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@TgAPI
class CreateChatSubscriptionInviteLinkAction(
    subscriptionPrice: Int,
    name: String? = null,
    subscriptionPeriod: Duration,
) : Action<ChatInviteLink>() {
    @TgAPI.Name("createChatSubscriptionInviteLink")
    override val method = "createChatSubscriptionInviteLink"
    override val returnType = getReturnType()

    init {
        parameters["subscription_price"] = subscriptionPrice.toJsonElement()
        parameters["subscription_period"] = subscriptionPeriod.encodeWith(DurationSerializer)
        if (name != null) parameters["name"] = name.toJsonElement()
    }
}

/**
 * Use this method to create a subscription invite link for a channel chat. The bot must have the can_invite_users administrator rights. The link can be edited using the method editChatSubscriptionInviteLink or revoked using the method revokeChatInviteLink. Returns the new invite link as a ChatInviteLink object.
 *
 * [Api reference](https://core.telegram.org/bots/api#createchatsubscriptioninvitelink)
 * @param chatId Unique identifier for the target channel chat or username of the target channel (in the format @channelusername)
 * @param name Invite link name; 0-32 characters
 * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment. Currently, it must always be 2592000 (30 days).
 * @param subscriptionPrice The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat; 1-2500
 * @returns [ChatInviteLink]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun createChatSubscriptionInviteLink(
    subscriptionPrice: Int,
    name: String? = null,
    subscriptionPeriod: Duration = 30.days,
) = CreateChatSubscriptionInviteLinkAction(subscriptionPrice, name, subscriptionPeriod)
