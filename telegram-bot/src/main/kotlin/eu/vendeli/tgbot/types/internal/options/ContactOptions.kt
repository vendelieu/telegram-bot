package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters

data class ContactOptions(
    var lastName: String? = null,
    var vcard: String? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null,
) : OptionsCommon
