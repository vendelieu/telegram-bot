package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.msg.SuggestedPostParameters
import kotlinx.serialization.Serializable

@Serializable
data class VideoNoteOptions(
    var duration: Int? = null,
    var length: Int? = null,
    override var thumbnail: ImplicitFile? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
    override var directMessagesTopicId: Int? = null,
    override var suggestedPostParameters: SuggestedPostParameters? = null,
) : OptionsCommon,
    ForumProps,
    ThumbnailProp,
    MessageEffectIdProp,
    AllowPaidBroadcastProp,
    DirectMessagesTopicProp,
    SuggestedPostParametersProp
