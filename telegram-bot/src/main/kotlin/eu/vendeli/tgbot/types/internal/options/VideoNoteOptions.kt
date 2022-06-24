package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.internal.ImplicitFile

data class VideoNoteOptions(
    var duration: Int? = null,
    var length: Int? = null,
    var thumb: ImplicitFile<*>? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<VideoNoteOptions>, OptionsCommon
