@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PromoteChatMemberOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class PromoteChatMemberAction(userId: Long) :
    Action<Boolean>(),
    OptionsFeature<PromoteChatMemberAction, PromoteChatMemberOptions> {
    override val method = TgMethod("promoteChatMember")
    override val returnType = getReturnType()
    override val options = PromoteChatMemberOptions()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

inline fun promoteChatMember(userId: Long) = PromoteChatMemberAction(userId)

inline fun promoteChatMember(user: User) = promoteChatMember(user.id)
