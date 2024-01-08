package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.LinkPreviewOptions
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters

interface Options

interface OptionsParseMode : Options {
    var parseMode: ParseMode?
}

interface MediaSpoiler : Options {
    var hasSpoiler: Boolean?
}

interface FileOptions : Options {
    var fileName: String?
}

interface LinkPreviewProp {
    var linkPreviewOptions: LinkPreviewOptions?

    fun disableWebPagePreview() {
        linkPreviewOptions = LinkPreviewOptions(isDisabled = true)
    }

    fun linkPreviewOptions(block: LinkPreviewOptions.() -> Unit) {
        linkPreviewOptions = LinkPreviewOptions().apply(block)
    }
}

interface OptionsCommon : Options {
    var disableNotification: Boolean?
    var replyParameters: ReplyParameters?
    var protectContent: Boolean?
    var messageThreadId: Long?

    fun replyParameters(messageId: Long, block: ReplyParameters.() -> Unit = {}) {
        replyParameters = ReplyParameters(messageId).apply(block)
    }
}
