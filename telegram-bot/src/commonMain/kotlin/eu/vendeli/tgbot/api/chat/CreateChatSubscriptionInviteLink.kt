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

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun createChatSubscriptionInviteLink(
    subscriptionPrice: Int,
    name: String? = null,
    subscriptionPeriod: Duration = 30.days,
) = CreateChatSubscriptionInviteLinkAction(subscriptionPrice, name, subscriptionPeriod)
