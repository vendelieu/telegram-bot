package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PromoteChatMemberOptions

class PromoteChatMemberAction(userId: Long) :
    Action<Boolean>,
    OptionAble,
    OptionsFeature<PromoteChatMemberAction, PromoteChatMemberOptions> {
    override val method: TgMethod = TgMethod("promoteChatMember")
    override var options = PromoteChatMemberOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun promoteChatMember(userId: Long) = PromoteChatMemberAction(userId)
