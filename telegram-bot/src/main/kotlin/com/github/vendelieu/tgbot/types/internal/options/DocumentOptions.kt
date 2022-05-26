package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode
import java.io.File

data class DocumentOptions(
    var thumb: File? = null,
    var disableContentTypeDetection: Boolean? = null,
    override var fileName: String? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<DocumentOptions>, IOptionsCommon, OptionsParseMode, IFileOptions
