package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile

data class DocumentOptions(
    var thumb: ImplicitFile<*>? = null,
    var disableContentTypeDetection: Boolean? = null,
    override var fileName: String? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null
) : OptionsInterface<DocumentOptions>, OptionsCommon, OptionsParseMode, FileOptions
