package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.LinkPreviewOptions
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.msg.SuggestedPostParameters
import kotlinx.serialization.Serializable

@Serializable
data class MessageOptions(
    override var linkPreviewOptions: LinkPreviewOptions? = null,
    override var parseMode: ParseMode? = null,
    override var protectContent: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
    override var directMessagesTopicId: Int? = null,
    override var suggestedPostParameters: SuggestedPostParameters? = null,
) : OptionsCommon,
    ForumProps,
    OptionsParseMode,
    LinkPreviewProp,
    MessageEffectIdProp,
    AllowPaidBroadcastProp,
    DirectMessagesTopicProp,
    SuggestedPostParametersProp
