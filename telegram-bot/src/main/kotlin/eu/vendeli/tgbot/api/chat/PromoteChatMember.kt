@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PromoteChatMemberOptions
import eu.vendeli.tgbot.utils.getReturnType

class PromoteChatMemberAction(userId: Long) :
    Action<Boolean>,
    ActionState(),
    OptionsFeature<PromoteChatMemberAction, PromoteChatMemberOptions> {
    override val method: TgMethod = TgMethod("promoteChatMember")
    override val returnType = getReturnType()
    override val OptionsFeature<PromoteChatMemberAction, PromoteChatMemberOptions>.options: PromoteChatMemberOptions
        get() = PromoteChatMemberOptions()

    init {
        parameters["user_id"] = userId
    }
}

fun promoteChatMember(userId: Long) = PromoteChatMemberAction(userId)
