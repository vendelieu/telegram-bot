package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters

data class GameOptions(
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Long? = null,
) : OptionsCommon
