package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.msg.SuggestedPostParameters
import kotlinx.serialization.Serializable

@Serializable
data class VoiceOptions(
    override var parseMode: ParseMode? = null,
    var duration: Int? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
    override var directMessagesTopicId: Int? = null,
    override var suggestedPostParameters: SuggestedPostParameters? = null,
) : OptionsCommon,
    ForumProps,
    OptionsParseMode,
    MessageEffectIdProp,
    AllowPaidBroadcastProp,
    DirectMessagesTopicProp,
    SuggestedPostParametersProp
