package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.LinkPreviewOptions
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.component.ImplicitFile
import kotlinx.serialization.Serializable

@Serializable
sealed interface Options

interface OptionsParseMode : Options {
    var parseMode: ParseMode?
}

interface MediaSpoiler : Options {
    var hasSpoiler: Boolean?
}

interface LinkPreviewProp : Options {
    var linkPreviewOptions: LinkPreviewOptions?

    fun disableWebPagePreview() {
        linkPreviewOptions = LinkPreviewOptions(isDisabled = true)
    }

    fun linkPreviewOptions(block: LinkPreviewOptions.() -> Unit) {
        linkPreviewOptions = LinkPreviewOptions().apply(block)
    }
}

interface ThumbnailProp : Options {
    var thumbnail: ImplicitFile?
}

interface MessageEffectIdProp : Options {
    var messageEffectId: String?
}

interface ShowCaptionAboveMediaProp : Options {
    var showCaptionAboveMedia: Boolean?
}

interface ForumProps : Options {
    var messageThreadId: Int?
}

@Serializable
sealed interface OptionsCommon : Options {
    var disableNotification: Boolean?
    var replyParameters: ReplyParameters?
    var protectContent: Boolean?
    var allowPaidBroadcast: Boolean?

    var replyToMessageId: Long?
        get() = replyParameters?.messageId
        set(value) {
            value?.also { replyParameters(it) }
        }

    fun replyParameters(messageId: Long, block: ReplyParameters.() -> Unit = {}) {
        replyParameters = ReplyParameters(messageId).apply(block)
    }
}
