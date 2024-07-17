package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters
import eu.vendeli.tgbot.types.internal.ImplicitFile
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
) : OptionsCommon,
    ThumbnailProp,
    MessageEffectIdProp
