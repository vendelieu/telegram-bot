package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.LinkPreviewOptions
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
sealed interface Options

interface OptionsParseMode : Options {
    var parseMode: ParseMode?
}

interface MediaSpoiler : Options {
    var hasSpoiler: Boolean?
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

interface MessageEffectIdProp {
    var messageEffectId: String?
}

@Serializable
sealed interface OptionsCommon : Options {
    var disableNotification: Boolean?
    var replyParameters: ReplyParameters?
    var protectContent: Boolean?
    var messageThreadId: Int?

    var replyToMessageId: Long?
        get() = replyParameters?.messageId
        set(value) {
            value?.also { replyParameters(it) }
        }

    fun replyParameters(messageId: Long, block: ReplyParameters.() -> Unit = {}) {
        replyParameters = ReplyParameters(messageId).apply(block)
    }
}
