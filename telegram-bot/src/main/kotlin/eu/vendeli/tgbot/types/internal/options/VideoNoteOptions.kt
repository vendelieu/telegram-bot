package eu.vendeli.tgbot.types.internal.options

import java.io.File

data class VideoNoteOptions(
    var duration: Int? = null,
    var length: Int? = null,
    var thumb: File? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<VideoNoteOptions>, OptionsCommon
